package com.hrm.services;

import com.hrm.dto.user.UserDto;
import com.hrm.entities.AppRole;
import com.hrm.entities.AppUser;
import com.hrm.payload.BaseResponse;
import com.hrm.payload.UserRequest;
import com.hrm.payload.userdto.*;
import com.hrm.repositories.ContractTypeRepository;
import com.hrm.repositories.LaborContractRepository;
import com.hrm.repositories.RoleRepository;
import com.hrm.repositories.UserRepository;
import com.hrm.security.AppUserDetails;
import com.hrm.security.JWTProvider;
import com.hrm.services.contract.AppUserService;
import com.hrm.services.contract.EmailService;
import com.hrm.utils.MapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service @RequiredArgsConstructor @Slf4j
public class AppUserServiceImpl implements AppUserService
{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JWTProvider JWTProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final LaborContractRepository laborContractRepository;
    private final ContractTypeRepository contractTypeRepository;
    private final EmailService emailService;
    private final MapperUtils mapperUtils;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse signUp(SignUpDto signUpDto)
    {
        try {
            if ( userRepository.existByName(signUpDto.getUsername()) ) throw new RuntimeException("Username already taken");
            if ( userRepository.existByEmail(signUpDto.getEmail()) ) throw new RuntimeException("Email already taken");
            var roles = roleRepository.findAllById(signUpDto.getRoles());
            var newUser = new AppUser()
                                  .setUsername(signUpDto.getUsername())
                                  .setEmail(signUpDto.getEmail())
                                  .setRoles(new HashSet<>(roles))
                                  .setPhone(signUpDto.getPhone())
                                  .setBankAccount(signUpDto.getBankAccount())
                                  .setPassword(passwordEncoder.encode(signUpDto.getPassword()));
            var createdUser = userRepository.save(newUser);
            log.info("User created");
            return BaseResponse.success(createdUser);
        }
        catch ( Exception e ) {
            log.error("Failed creating user: " + e.getMessage());
            return BaseResponse.error(e.getMessage());
        }
    }

    /*
     * For checking username from the frontend
     * */
    public BaseResponse checkUserName(String username)
    {
        return userRepository.existByName(username)
               ? BaseResponse.error("Username has already been taken")
               : BaseResponse.success();
    }

    /*
     *For checking email from the frontend
     * */
    public BaseResponse checkEmail(String email)
    {
        return userRepository.existByEmail(email)
               ? BaseResponse.error("Email has already been taken")
               : BaseResponse.success();
    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public BaseResponse guestSignUp(GuestSignUpDto signUpDto, HttpServletRequest request)
//    {
//        try {
//            if ( userRepository.existByName(signUpDto.getUsername()) ) throw new RuntimeException("Username already taken");
//            if ( userRepository.existByEmail(signUpDto.getEmail()) ) throw new RuntimeException("Email already taken");
//            var role = roleRepository.findByName("ROLE_REGISTERED");
//            var newGuest = new GuestAccount()
//                                   .setFirstName(signUpDto.getFirstName())
//                                   .setLastName(signUpDto.getLastName());
//            newGuest = guestAccountRepository.save(newGuest);
//            var verificationCode = RandomString.make(128);
//            var newUser = (AppUser) new AppUser()
//                                            .setUsername(signUpDto.getUsername())
//                                            .setEmail(signUpDto.getEmail())
//                                            .setPassword(passwordEncoder.encode(signUpDto.getPassword()))
//                                            .setRoles(new HashSet<>(Collections.singletonList(role)))
//                                            .setGuestAccount(newGuest)
//                                            .setIsEnabled(false)
//                                            .setVerificationCode(verificationCode)
//                                            .setVerifyCodeExpiration(LocalDateTime.now().plusHours(24))
//                                            .setCreatedTime(LocalDateTime.now());
//            sendVerificationCode(signUpDto, request);
//            return BaseResponse.success(userRepository.save(newUser));
//        }
//        catch ( Exception e ) {
//            log.error("Failed creating user: " + e.getMessage());
//            return BaseResponse.error(e.getMessage());
//        }
//    }

    @Async
    public CompletableFuture<VerifyResponse> activateAccount(String verificationCode)
    {
        try {
            var user = userRepository.findByVerificationCode(verificationCode);
            if ( user != null && user.getVerifyCodeExpiration().isBefore(LocalDateTime.now()) ) {
                user.setIsActivated(true);
                user.setVerificationCode(null);
                user.setVerifyCodeExpiration(null);
                var roles = user.getRoles();
                roles.add(roleRepository.findByName("ROLE_GUEST"));
                user.setRoles(roles);
                userRepository.save(user);
                return CompletableFuture.completedFuture(
                        VerifyResponse.builder()
                                      .message("Verification successful")
                                      .succeed(true)
                                      .build()
                );
            }
            return CompletableFuture.completedFuture(
                    VerifyResponse.builder()
                                  .message("Can not verify your account. Please request new verification code")
                                  .succeed(false)
                                  .build()
            );
        }
        catch ( Exception e ) {
            return CompletableFuture.completedFuture(
                    VerifyResponse.builder()
                                  .message(e.getMessage())
                                  .succeed(false)
                                  .build()
            );
        }
    }

    @Override
    public CompletableFuture<TokenResponse> signIn(SignInDto signInDto, HttpServletRequest req)
    {
        try {
            var auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            signInDto.getUsername(),
                            signInDto.getPassword()));
            if ( auth.isAuthenticated() ) {
                var userDetails = (AppUserDetails) userDetailsService.loadUserByUsername(signInDto.getUsername());
                var token = JWTProvider.createToken(userDetails);
                var refreshToken = createRefreshToken();
                updateRefreshToken(signInDto.getUsername(), refreshToken);
                return CompletableFuture.completedFuture(
                        new TokenResponse().setUsername(signInDto.getUsername())
                                .setUserId(userDetails.getUserId())
                                .setAccessToken(token)
                                .setRefreshToken(refreshToken.getToken())
                                .setExpiration(JWTProvider.getExpiration(token))
                                .setCode("200")
                                .setMessage("Successfully logged in")
                );
            }
            return CompletableFuture.completedFuture(
                    new TokenResponse().setMessage("Bad credential")
                            .setSucceed(false)
                            .setCode("401")
            );
        }
        catch ( Exception e ) {
            return CompletableFuture.completedFuture(
                    new TokenResponse().setMessage(e.getMessage())
                            .setSucceed(false)
                            .setCode("401")
            );
        }
    }

    @Override
    public RefreshToken createRefreshToken()
    {
        var refreshToken = new RefreshToken();
        refreshToken.setToken(Base64.getEncoder()
                                    .encodeToString(UUID.randomUUID()
                                                        .toString()
                                                        .getBytes()));
        refreshToken.setExpireDate(LocalDateTime.now().plusDays(1));
        return refreshToken;
    }

    @Override
    public void updateRefreshToken(String username, RefreshToken token)
    {
        userRepository.updateRefreshToken(username, token.getToken(), token.getExpireDate());
    }

    @Override
    public BaseResponse getUserList(Integer page, Integer size)
    {
        var listUsers = userRepository.findAllNonDeleted(PageRequest.of(page, size));
        var res = listUsers.map(user -> new UserDisplayDto()
                                                .setId(user.getId())
                                                .setUsername(user.getUsername())
                                                .setEmail(user.getEmail()));
        return BaseResponse.success(res);
    }

    public BaseResponse selfChangePassword(String oldPassword,
                                           String newPassword,
                                           UUID userId,
                                           PasswordEncoder passwordEncoder,
                                           Authentication authentication)
    {
        if ( !authentication.isAuthenticated() ) return BaseResponse.error("Not signed in");
        var userDetails = (AppUserDetails) authentication.getPrincipal();

        var user = userRepository.findById(userId).orElse(null);
        if ( user == null ) return BaseResponse.error("User not found");
        if ( !userDetails.getUserId().toString().equals(userId.toString()) ) return BaseResponse.error("UserId does not match");

        if ( passwordEncoder.matches(oldPassword, userDetails.getPassword()) ) {
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return BaseResponse.success("Password changed. Please sign off and then sign in again.");
        }
        return BaseResponse.error("Can not change password");
    }

//    private void sendVerificationCode(GuestSignUpDto signUpDto, HttpServletRequest request)
//            throws ExecutionException, InterruptedException
//    {
//        String body = """
//                Dear [[name]],<br>
//                Please click the link below to verify your registration:<br>
//                <h3><a href=\\"[[URL]]\\" target=\\"_self\\">VERIFY</a></h3>
//                Thank you,<br>
//                MyBooking Co., ltd
//                """;
//        var siteURL = request.getRequestURL()
//                             .toString()
//                             .replace(request.getServletPath(), "");
//
//        body = body.replace("[[name]]", signUpDto.getUsername());
//        body = body.replace("[[URL]]", siteURL);
//        var message = EmailDetails.builder()
//                                  .recipient(signUpDto.getEmail())
//                                  .msgBody(body)
//                                  .subject("Verification email")
//                                  .build();
//        emailService.sendFormattedEmail(message).get();
//    }

    public UserDto convertUserToDto(AppUser appUser)
    {
        var userDto = new UserDto()
        .setId(appUser.getId())
        .setUsername(appUser.getUsername())
        .setFullName(appUser.getFullName())
        .setEmail(appUser.getEmail())
        .setPhone(appUser.getPhone())
        .setAddress(appUser.getAddress())
        .setBankAccount(appUser.getBankAccount())
        .setBankFullName(appUser.getBankFullName())
        .setBankShortName(appUser.getBankShortName());
        for(AppRole role : appUser.getRoles()){
            userDto.setRole(role.getRoleName());
        }
        var isActivated = appUser.getIsActivated()
                ? "Hoạt động" : "Không hoạt động";
        userDto.setIsActivated(isActivated);
        return userDto;
    }

    public BaseResponse getAllUser(Integer page, Integer size)
    {
        var listUsers = userRepository.findAllNonDeleted(PageRequest.of(page, size));
        var res = listUsers.map(this::convertUserToDto);
        return BaseResponse.success(res);
    }

    public BaseResponse getAllEpl(){
        List<Long> rolesId = Arrays.asList(1L ,4L);
        var listEpl = userRepository.getListEpl();
        var rolesAdmin = roleRepository.findAllById(rolesId);
//        var listEplDto = listEpl.stream().filter(s -> {
//                    return s.getRoles().equals(new HashSet<>(rolesAdmin));
//        });
        var listEplDto = listEpl.stream().map(this::convertUserToDto);
        return BaseResponse.success(listEplDto);
    }

    public UserDto getUser(UUID id) throws Exception
    {
        return userRepository.findById(id).map(this::convertUserToDto)
                             .orElseThrow(() -> new Exception("User not found"));
    }

    @Transactional(rollbackFor = Exception.class) @Async
    public CompletableFuture<BaseResponse> deletedUser(UUID userId, Authentication authentication){
        try {
            var userExist = userRepository.findById(userId).orElseThrow(() ->
                    new RuntimeException("User invalid by id: " + userId));
            userExist.setIsDeleted(true)
                    .setModification(authentication);
            var contractExistByUser = laborContractRepository.findByCurrentContract(userId, true);
            contractExistByUser.ifPresent(ct -> {
                ct.setIsActivated(false);
                ct.setModification(authentication);
            });
            log.info("Delete user successfully!");
            return CompletableFuture.completedFuture(
                    BaseResponse.success("Delete user successfully!")
            );
        } catch (Exception ex){
            log.error("Delete user fail: " + ex.getMessage());
            return CompletableFuture.completedFuture(
                    BaseResponse.error(ex.getMessage())
            );
        }
    }

    public BaseResponse getAllUserDepartment(Integer page, Integer size){
        try {
            var listObj = userRepository.getAllUserDepartmentNodeleted(PageRequest.of(page, size, Sort.by("id").descending()));
            var logMessage = listObj != null
                    ? "Get list user department oke"
                    : "List user department is empty";
            log.info(logMessage);
            return listObj != null
                    ? BaseResponse.success(listObj).setMessage("Get list user department oke")
                    : BaseResponse.success("List user department is empty");
        } catch (Exception ex){
            log.error("Get list user department fail! " + ex.getMessage());
            return  BaseResponse.error(ex.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseResponse createUser(UserRequest request, Authentication authentication){
        try {
//            var userExist = userRepository.findById(request.getId())
//                    .orElseThrow(() -> new RuntimeException("User is already exist!"));
            var userNameExist = userRepository.findByUsername(request.getUserName());
            if (userNameExist != null) throw new RuntimeException("Username is already exist!");
            List<Long> ids = Arrays.asList(2L, 3L, 4L);
            var roles = roleRepository.findAllById(ids);

//            List<Long> ids2 = Arrays.asList(request.getRolesId().get(0)) ;
            var roles2 = roleRepository.findAllById(request.getRolesId());
//                    .orElseThrow(() -> new RuntimeException("Role invalid by id" + request.getRoleId()));
//            Set<AppRole> linkedHashSet = new LinkedHashSet<>();
            var userCreate = new AppUser()
                    .setUsername(request.getUserName())
                    .setFullName(request.getFullName())
                    .setPassword(passwordEncoder.encode(request.getPassword()))
                    .setEmail(request.getEmail())
                    .setPhone(request.getPhone())
                    .setAddress(request.getAddress())
                    .setBankAccount(request.getBankAccount())
                    .setBankFullName(request.getBankFullName())
                    .setBankShortName(request.getBankShortName())
                    .setRoles(new HashSet<>(roles2));
            userCreate.setCreation(authentication);
            var userSave = userRepository.save(userCreate);
            log.info("Create user successfully!");
            return BaseResponse.success(userSave).setMessage("Create user successfully!");
        } catch (Exception ex){
            log.error("Create user fail" + ex.getMessage());
            return BaseResponse.error(ex.getMessage());
        }
    }

//    public BaseResponse updateUser(UserRequest request, Authentication authentication) {
//        try {
//            var userExist = userRepository.findById(request.getId())
//                    .orElseThrow(() -> new RuntimeException("User invalid by id: "+request.getId()));
//            userExist.setUsername(request.getUserName()|)
//                    .setFullName(request.getFullName())
//                    .setPassword(request.getPassword())
//                    .setEmail(request.getEmail())
//
//        } catch (Exception ex) {
//
//        }
//    }
}

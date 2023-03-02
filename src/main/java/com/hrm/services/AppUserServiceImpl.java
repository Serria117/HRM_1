package com.hrm.services;

import com.hrm.entities.AppUser;
import com.hrm.payload.BaseResponse;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service @RequiredArgsConstructor @Slf4j
public class AppUserServiceImpl implements AppUserService
{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JWTProvider jwtProvider;
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
            log.info("New user created");
            return BaseResponse.success(new UserDisplayDto(createdUser));
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
    public TokenResponse signIn(SignInDto signInDto)
    {
        try {
            var auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            signInDto.getUsername(),
                            signInDto.getPassword()));
            if ( auth.isAuthenticated() ) {
                var userDetails = (AppUserDetails) userDetailsService.loadUserByUsername(signInDto.getUsername());
                var token = jwtProvider.createToken(userDetails);
                var refreshToken = createRefreshToken();
                updateRefreshToken(signInDto.getUsername(), refreshToken);
                return new TokenResponse().setUsername(signInDto.getUsername())
                                          .setUserId(userDetails.getUserId())
                                          .setAccessToken(token)
                                          .setRefreshToken(refreshToken.getToken())
                                          .setExpiration(jwtProvider.getExpiration(token))
                                          .setRoles(new LinkedHashSet<>(jwtProvider.getRoleFromUser(userDetails)))
                                          .setAuthorities(new LinkedHashSet<>(jwtProvider.getAuthorityFromUser(userDetails)))
                                          .setCode("200")
                                          .setMessage("Successfully logged in");
            }
            return new TokenResponse().setMessage("Bad credential")
                                      .setSucceed(false)
                                      .setCode("401");
        }
        catch ( Exception e ) {
            return new TokenResponse().setMessage(e.getMessage())
                                      .setSucceed(false)
                                      .setCode("401");
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
        var res = listUsers.map(user -> new UserDisplayDto(user)
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
        if ( !userDetails.getUserId().toString().equals(userId.toString()) )
            return BaseResponse.error("UserId does not match");

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
}

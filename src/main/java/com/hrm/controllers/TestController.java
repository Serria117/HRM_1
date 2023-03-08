package com.hrm.controllers;

import com.hrm.configurations.SwaggerConfig;
import com.hrm.repositories.UserRepository;
import com.hrm.security.JWTProvider;
import com.hrm.services.AppUserServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController @RequestMapping("/api/test")
@SecurityRequirement(name = SwaggerConfig.SECURITY_NAME)
@Slf4j
public class TestController
{
    private final UserRepository userRepository;
    private final AppUserServiceImpl userService;
    private final JWTProvider jwtProvider;

    public TestController(UserRepository userRepository,
                          AppUserServiceImpl userService,
                          JWTProvider jwtProvider)
    {
        this.userRepository = userRepository;
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    @GetMapping("role")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<String> getCurrentUserRole(Authentication authentication)
    {
        return authentication.getAuthorities()
                             .stream()
                             .map(GrantedAuthority::getAuthority).toList();
    }


    @GetMapping("user")
    public ResponseEntity<?> getUserInfo(Authentication authentication,
                                         HttpServletRequest request)
    {
        try {
            if(authentication.isAuthenticated()){
                log.info("Found user in security context: " + authentication.getName());
                var userId = userService.getUserIdFromRequest(request);
                log.info("With userId: " + userId);
                var user = userRepository.findById(userId);
                return ResponseEntity.ok(user);
            }
            log.info("No user in current security context.");
            return ResponseEntity.notFound().build();
        }
        catch ( Exception e ) {
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("user/get-all")
    public ResponseEntity<?> getAllUser(@RequestParam(required = false, defaultValue = "0") Integer page,
                                        @RequestParam(required = false, defaultValue = "100") Integer size)
    {
        var res = userService.getUserList(page, size);
        return ResponseEntity.ok(res);
    }
}

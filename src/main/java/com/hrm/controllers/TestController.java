package com.hrm.controllers;

import com.hrm.configurations.SwaggerConfig;
import com.hrm.repositories.UserRepository;
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

import java.util.List;

@RestController @RequestMapping("/api/test")
@SecurityRequirement(name = SwaggerConfig.SECURITY_NAME)
@Slf4j
public class TestController
{
    private final UserRepository userRepository;
    private final AppUserServiceImpl userService;

    public TestController(UserRepository userRepository, AppUserServiceImpl userService)
    {
        this.userRepository = userRepository;
        this.userService = userService;
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
    public ResponseEntity<?> getUserInfo(Authentication authentication)
    {
        var user = userRepository.findByUsername(authentication.getName());
        if ( user != null ) return ResponseEntity.ok(user);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("user/get-all")
    public ResponseEntity<?> getAllUser(@RequestParam(required = false, defaultValue = "0") Integer page,
                                        @RequestParam(required = false, defaultValue = "100") Integer size)
    {
        var res = userService.getUserList(page, size);
        return ResponseEntity.ok(res);
    }
}

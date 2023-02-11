package com.hrm.controllers;

import com.hrm.configurations.SwaggerConfig;
import com.hrm.repositories.UserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController @RequestMapping("/api/test")
@SecurityRequirement(name = SwaggerConfig.SECURITY_NAME)
public class TestController
{
    @Autowired UserRepository userRepository;

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
        if(user != null) return ResponseEntity.ok(user);
        return ResponseEntity.notFound().build();
    }
}

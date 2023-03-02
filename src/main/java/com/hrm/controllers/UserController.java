package com.hrm.controllers;

import com.hrm.configurations.SwaggerConfig;
import com.hrm.services.AppUserServiceImpl;
import com.hrm.services.UserServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/user")
@SecurityRequirement(name = SwaggerConfig.SECURITY_NAME)
@Slf4j
public class UserController {

    private final AppUserServiceImpl userService;

    public UserController(AppUserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping("get-all")
    public ResponseEntity<?> getAllUser(@RequestParam(required = false, defaultValue = "0") Integer page,
                                        @RequestParam(required = false, defaultValue = "100") Integer size)
    {
        var res = userService.getAllUser(page, size);
        return ResponseEntity.ok(res);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getUser(@PathVariable(name = "id") UUID id) throws Exception {
        var userRes = userService.getUser(id);
        return userRes != null ? ResponseEntity.ok(userRes) : ResponseEntity.badRequest().build();
    }
}

package com.hrm.controllers;

import com.hrm.configurations.SwaggerConfig;
import com.hrm.payload.UserRequest;
import com.hrm.services.AppUserServiceImpl;
import com.hrm.services.UserServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

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

    @GetMapping("get-listEpl")
    public ResponseEntity<?> getListEpl(){
        var res = userService.getAllEpl();
        return  res.getSucceed()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getUser(@PathVariable(name = "id") UUID id) throws Exception {
        var userRes = userService.getUser(id);
        return userRes != null ? ResponseEntity.ok(userRes) : ResponseEntity.badRequest().build();
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody UserRequest request, Authentication authentication){
        var res = userService.createUser(request, authentication);
        return res.getSucceed()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }
    @PostMapping("/delete/{id}") @Async
    public CompletableFuture<ResponseEntity<?>> deletedUser(@PathVariable(name = "id") UUID userId, Authentication authentication) throws ExecutionException, InterruptedException {
        var res = userService.deletedUser(userId, authentication);
        return CompletableFuture.completedFuture(
                res.get().getSucceed()
                        ? ResponseEntity.ok(res)
                        : ResponseEntity.badRequest().body(res)
        );
    }

    @GetMapping("/get-listUserDepartment")
    public ResponseEntity<?> getListUserDepartment(@RequestParam(required = false, defaultValue = "0") Integer page,
                                                   @RequestParam(required = false, defaultValue = "100") Integer size){
        var res = userService.getAllUserDepartment(page, size);
        return res.getSucceed()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }
}

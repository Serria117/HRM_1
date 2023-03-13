package com.hrm.controllers;

import com.hrm.payload.userdto.SignInDto;
import com.hrm.payload.userdto.SignUpDto;
import com.hrm.payload.userdto.TokenResponse;
import com.hrm.services.contract.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;

@RestController @RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController
{
    private final AppUserService userService;

    @PostMapping("sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpDto signUpDto)
    {
        var res = userService.signUp(signUpDto);
        return res.getSucceed()
               ? ResponseEntity.ok(res)
               : ResponseEntity.badRequest().body(res);
    }

    @PostMapping("sign-in") @Async
    public CompletableFuture<TokenResponse> signIn(@RequestBody SignInDto signInDto, HttpServletRequest req)
    {
        return userService.signIn(signInDto, req);
    }
}

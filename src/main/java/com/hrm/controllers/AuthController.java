package com.hrm.controllers;

import com.hrm.payload.userdto.SignInDto;
import com.hrm.payload.userdto.SignUpDto;
import com.hrm.services.contract.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInDto signInDto)
    {
        var res = userService.signIn(signInDto);
        return res.getSucceed()
               ? ResponseEntity.ok(res)
               : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(res);
    }
}

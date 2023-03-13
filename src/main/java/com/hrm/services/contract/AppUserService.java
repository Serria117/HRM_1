package com.hrm.services.contract;

import com.hrm.payload.BaseResponse;
import com.hrm.payload.userdto.RefreshToken;
import com.hrm.payload.userdto.SignInDto;
import com.hrm.payload.userdto.SignUpDto;
import com.hrm.payload.userdto.TokenResponse;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.CompletableFuture;

public interface AppUserService
{
    @Transactional(rollbackFor = Exception.class)
    BaseResponse signUp(SignUpDto signUpDto);

    CompletableFuture<TokenResponse> signIn(SignInDto signInDto, HttpServletRequest req);

    RefreshToken createRefreshToken();

    void updateRefreshToken(String username, RefreshToken token);

    BaseResponse getUserList(Integer page, Integer size);
}

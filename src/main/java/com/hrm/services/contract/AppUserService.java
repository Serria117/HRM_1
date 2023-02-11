package com.hrm.services.contract;

import com.hrm.payload.BaseResponse;
import com.hrm.payload.userdto.RefreshToken;
import com.hrm.payload.userdto.SignInDto;
import com.hrm.payload.userdto.SignUpDto;
import com.hrm.payload.userdto.TokenResponse;
import org.springframework.transaction.annotation.Transactional;

public interface AppUserService
{
    @Transactional(rollbackFor = Exception.class)
    BaseResponse signUp(SignUpDto signUpDto);

    TokenResponse signIn(SignInDto signInDto);

    RefreshToken createRefreshToken();

    void updateRefreshToken(String username, RefreshToken token);
}

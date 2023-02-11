package com.hrm.payload.userdto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data @Accessors(chain = true)
public class TokenResponse
{
    String username;
    String code;
    String message;
    String accessToken;
    Date expiration;
    String refreshToken;
    Boolean succeed = true;
}

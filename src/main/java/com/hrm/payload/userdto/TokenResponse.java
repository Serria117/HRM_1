package com.hrm.payload.userdto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.UUID;

@Data @Accessors(chain = true)
public class TokenResponse
{
    String username;
    UUID userId;
    String code;
    String message;
    String accessToken;
    Date expiration;
    String refreshToken;
    Boolean succeed = true;
}

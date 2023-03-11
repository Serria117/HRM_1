package com.hrm.payload.userdto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collection;
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
    Collection<String> roles;
    Collection<String> authorities;
    Date expiration;
    String refreshToken;
    String loginIP;
    Boolean succeed = true;
}

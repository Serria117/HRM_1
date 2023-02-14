package com.hrm.payload.userdto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.*;

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
    Set<String> roles = new LinkedHashSet<>();
    Set<String> authorities = new LinkedHashSet<>();
}

package com.hrm.payload.userdto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data @Accessors(chain = true)
public class RefreshToken
{
    String token;
    LocalDateTime expireDate;

}

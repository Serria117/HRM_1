package com.hrm.payload.userdto;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class VerifyResponse
{
    String message;
    boolean succeed;
}

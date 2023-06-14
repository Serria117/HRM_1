package com.hrm.payload.userdto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class SignInDto
{
    @NotBlank
    String username;
    @NotBlank
    String password;
}

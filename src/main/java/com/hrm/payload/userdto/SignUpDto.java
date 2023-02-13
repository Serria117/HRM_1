package com.hrm.payload.userdto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.Collection;

@Data
public class SignUpDto
{
    @NotBlank @Length(max = 100, min = 3, message = "Username must has minimum of 3 and maximum of 100 characters")
    String username;
    @NotBlank @Length(max = 60, min = 4, message = "Password must has minimum of 4 and maximum of 60 characters")
    String password;
    @Email(message = "A valid email address is required")
    String email;
    @Pattern(regexp = "^[0-9]{8,12}$")
    String phone;
    String bankAccount;
    Collection<Long> roles;
}

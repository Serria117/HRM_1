package com.hrm.payload.userdto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.Collection;

@Data
public class SignUpDto
{
    @NotBlank @Length(max = 100, min = 3, message = "Username must has minimum 3 and maximum 100 characters")
    String username;
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{4,}$",
            message = "Password must contain minimum 4 characters, at least 1 uppercase letter, 1 lowercase letter and 1 number")
    String password;
    @Email(message = "A valid email address is required")
    String email;
    @Pattern(regexp = "^[0-9]{8,12}$")
    String phone;
    String bankAccount;
    Collection<Long> roles;
}

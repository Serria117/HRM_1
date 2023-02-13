package com.hrm.payload.userdto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.UUID;

@Getter @Setter @Accessors(chain = true)
public class UserDisplayDto implements Serializable
{
    UUID id;
    String username;
    String email;
    String phone;
    String address;
    String bankAccount;
    String fullName;
}

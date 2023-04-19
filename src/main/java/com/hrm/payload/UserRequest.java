package com.hrm.payload;

import com.hrm.entities.AppRole;
import lombok.Data;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
public class UserRequest{
    private UUID id;
    private String userName;
    private String fullName;
    private String password;
    private String email;
    private String phone;
    private String address;
    private String bankAccount;
    private String bankFullName;
    private String bankShortName;
    private Collection<Long> rolesId;
}

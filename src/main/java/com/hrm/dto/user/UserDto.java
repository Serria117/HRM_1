package com.hrm.dto.user;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data @Getter @Setter @Builder @Accessors(chain = true)
@AllArgsConstructor @NoArgsConstructor
public class UserDto implements Serializable {
    private UUID id;
    private String username;
    private String fullName;
    private List<String> role;
    private String email;
    private String phone;
    private String departmentName;
    private String address;
    private String bankAccount;
    private String bankFullName;
    private String bankShortName;
    private String isActivated;
}

package com.hrm.payload.userdto;

import com.hrm.entities.AppAuthority;
import com.hrm.entities.AppRole;
import com.hrm.entities.AppUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter @Setter @Accessors(chain = true) @NoArgsConstructor
public class UserDisplayDto implements Serializable
{
    UUID id;
    String username;
    String email;
    String phone;
    String address;
    String bankAccount;
    String fullName;
    Set<String> roles = new HashSet<>();
    Set<String> authorities = new HashSet<>();

    public UserDisplayDto(AppUser user)
    {
        var roles = user.getRoles();
        var authorities = roles.stream()
                               .flatMap(r -> r.getAppAuthorities().stream())
                               .collect(Collectors.toSet());

        this.roles = roles.stream()
                          .map(AppRole::getRoleName)
                          .collect(Collectors.toSet());

        this.authorities = authorities.stream()
                                      .map(AppAuthority::getName)
                                      .collect(Collectors.toSet());
    }
}

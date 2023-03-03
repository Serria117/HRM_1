package com.hrm.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity @Table(name = "user")
@Getter @Setter @Accessors(chain = true)
public class AppUser extends BaseEntity implements Serializable
{
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "binary(16)")
    UUID id = UUID.randomUUID();
    @Size(min = 3, max = 100)
    String username;
    @Size(min = 3, max = 255)
    String fullName;
    String password;
    @Email
    String email;
    @Pattern(regexp = "^[0-9]{10,12}$")
    String phone;
    String address;
    @Pattern(regexp = "^[0-9]{3,50}$")
    String bankAccount;
    @Size(min = 3, max = 100)
    String bankFullName;
    @Size(min = 3, max = 10)
    String bankShortName;
    Byte[] biometricData;
    String refreshToken;
    LocalDateTime refreshTokenExpiration;
    LocalDateTime lastLoginTime;
    Boolean isPasswordReset = false;
    String lastLoginIP;
    String verificationCode;
    LocalDateTime verifyCodeExpiration;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "user_role",
            /*joinColumns = @JoinColumn(name = "user_id"),*/
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<AppRole> roles = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o)
    {
        if ( this == o ) return true;
        if ( o == null || Hibernate.getClass(this) != Hibernate.getClass(o) ) return false;
        AppUser appUser = (AppUser) o;
        return id != null && Objects.equals(id, appUser.id);
    }

    @Override
    public int hashCode()
    {
        return getClass().hashCode();
    }
}

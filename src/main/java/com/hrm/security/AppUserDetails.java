package com.hrm.security;

import com.hrm.entities.AppUser;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter @Setter @Accessors(chain = true)
public class AppUserDetails implements UserDetails
{
    AppUser currentUser;
    UUID userId;

    public AppUserDetails(AppUser user){
        this.currentUser = user;
        this.userId = user.getId();
    }

    public AppUserDetails setUserId(){
        this.userId = this.currentUser.getId();
        return this;
    }

    public UUID getUserId(){
        return this.userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        var authorities = currentUser.getRoles()
                                  .stream()
                                  .flatMap(role -> role.getAppAuthorities().stream()
                                                           .map(a -> new SimpleGrantedAuthority(a.getName())))
                                  .collect(Collectors.toList());
        authorities.addAll(getRoles());
        return authorities;
    }

    private Collection<SimpleGrantedAuthority> getRoles()
    {
        return currentUser.getRoles()
                          .stream()
                          .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                          .toList();
    }

    @Override
    public String getPassword()
    {
        return currentUser.getPassword();
    }

    @Override
    public String getUsername()
    {
        return currentUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return currentUser.getIsActivated();
    }

    @Override
    public boolean isEnabled()
    {
        return currentUser.getIsActivated();
    }
}

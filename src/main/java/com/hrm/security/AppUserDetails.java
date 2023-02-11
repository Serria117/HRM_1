package com.hrm.security;

import com.hrm.entities.AppUser;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@AllArgsConstructor @NoArgsConstructor
public class AppUserDetails implements UserDetails
{
    AppUser currentUser;

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

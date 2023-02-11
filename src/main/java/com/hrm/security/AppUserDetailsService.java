package com.hrm.security;

import com.hrm.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class AppUserDetailsService implements UserDetailsService
{
    @Autowired UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        var foundUser = userRepository.findByUsername(username);
        if ( foundUser == null ) throw new UsernameNotFoundException("Username does not exist");
        return new AppUserDetails(foundUser);
    }
}

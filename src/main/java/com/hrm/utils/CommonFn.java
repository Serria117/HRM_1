package com.hrm.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;

import javax.naming.AuthenticationException;

@UtilityClass
public class CommonFn
{
    public String getUsername(Authentication auth)
    {
        if ( auth.isAuthenticated() ) {
            return auth.getName();
        }
        return "<No user logged in>";
    }

    public void checkAuthentication(Authentication auth) throws AuthenticationException
    {
        if ( !auth.isAuthenticated() ) throw new AuthenticationException("No authentication found");
    }
}

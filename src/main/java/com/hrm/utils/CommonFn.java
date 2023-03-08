package com.hrm.utils;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;

import javax.naming.AuthenticationException;
import java.util.UUID;

@UtilityClass
public class CommonFn
{
    public String getUsername(Authentication auth) throws AuthenticationException
    {
        checkAuthentication(auth);
        return auth.getName();
    }

    public void checkAuthentication(Authentication auth) throws AuthenticationException
    {
        if ( !auth.isAuthenticated() ) throw new AuthenticationException("No authentication found");
    }


    public UUID stringToUUID(String str)
    {
        return UUID.fromString(str);
    }
}

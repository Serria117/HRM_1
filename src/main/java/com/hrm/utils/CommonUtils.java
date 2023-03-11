package com.hrm.utils;

import com.hrm.security.AppUserDetails;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;

import java.util.UUID;

@UtilityClass
public class CommonUtils
{
    public String getUsername(Authentication auth)
    {
        if ( auth.isAuthenticated() ) {
            return auth.getName();
        }
        return "<No user logged in>";
    }

    public UUID getUserId(Authentication auth)
    {
        if ( auth.isAuthenticated() ) {
            var user = (AppUserDetails) auth.getPrincipal();
            return user.getUserId();
        }
        return null;
    }
}

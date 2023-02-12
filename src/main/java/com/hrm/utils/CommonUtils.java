package com.hrm.utils;

import org.springframework.security.core.Authentication;

public class CommonUtils
{
    public static String getUsername(Authentication auth)
    {
        if ( auth.isAuthenticated() ) {
            return auth.getName();
        }
        return "<No user logged in>";
    }
}

package com.hrm.security;

public class AuthorityStore
{
    public static final String[] authorities = {
            "CREATE_ROLE", "READ_ROLE", "UPDATE_ROLE", "DELETE_ROLE",
            "CREATE_USER", "READ_USER", "UPDATE_USER", "DELETE_USER",
            "CREATE_DEP", "READ_DEP", "UPDATE_DEP", "DELETE_DEP"
    };
}

package com.hrm.customExpeption;

public class InvalidIdentityException extends RuntimeException
{
    public InvalidIdentityException(String message)
    {
        super(message);
    }
}

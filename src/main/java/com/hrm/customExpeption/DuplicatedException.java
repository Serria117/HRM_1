package com.hrm.customExpeption;

public class DuplicatedException extends RuntimeException
{
    public DuplicatedException(String message)
    {
        super(message);
    }
}

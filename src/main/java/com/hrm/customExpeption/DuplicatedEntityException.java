package com.hrm.customExpeption;

public class DuplicatedEntityException extends RuntimeException
{
    public DuplicatedEntityException(String message)
    {
        super(message);
    }
}

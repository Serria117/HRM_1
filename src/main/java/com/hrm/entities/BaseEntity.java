package com.hrm.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.core.Authentication;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass @Getter @Setter @Accessors(chain = true)
public abstract class BaseEntity
{
    protected Boolean isActivated = true;
    protected Boolean isDeleted = false;
    protected LocalDateTime createdTime = LocalDateTime.now();
    protected LocalDateTime lastModifiedTime;
    protected String createdByUser;
    protected String lastModifiedByUser;

    public void setCreation(Authentication authentication)
    {
        this.createdByUser = authentication.getName();
        this.createdTime = LocalDateTime.now();
    }

    public void setModification(Authentication authentication)
    {
        this.lastModifiedByUser = authentication.getName();
        this.lastModifiedTime = LocalDateTime.now();
    }
}

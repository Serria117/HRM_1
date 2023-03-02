package com.hrm.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.Authentication;

import javax.annotation.PostConstruct;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter @Setter @Accessors(chain = true)
public abstract class BaseEntity
{
    protected Boolean isActivated = true;
    protected Boolean isDeleted = false;
    @CreationTimestamp
    @Column(updatable = false)
    protected LocalDateTime createdTime;
    @UpdateTimestamp
    protected LocalDateTime lastModifiedTime;
    protected String createdByUser;
    protected String lastModifiedByUser;

    @PostConstruct
    public void setCreation(Authentication authentication)
    {
        this.createdByUser = authentication.getName();
        this.lastModifiedByUser = authentication.getName();
    }

    public void setModification(Authentication authentication)
    {
        this.lastModifiedByUser = authentication.getName();
    }
}

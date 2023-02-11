package com.hrm.services.contract;

import com.hrm.entities.BaseEntity;
import com.hrm.utils.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;

public abstract class BaseAppService
{
    @Autowired
    protected MapperUtils mapperUtils;

    protected <T extends BaseEntity> T setCreatedStamp(T entity, Authentication authentication)
    {
        if ( entity == null ) {return null;}
        entity.setCreatedTime(LocalDateTime.now());
        entity.setCreatedByUser(authentication.getName());
        return entity;
    }

    protected <T extends BaseEntity> T setUpdatedUser(T entity, Authentication authentication)
    {
        if ( entity == null ) {return null;}
        entity.setLastModifiedTime(LocalDateTime.now());
        entity.setLastModifiedByUser(authentication.getName());
        return entity;
    }
}

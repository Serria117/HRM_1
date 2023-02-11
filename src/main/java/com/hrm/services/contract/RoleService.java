package com.hrm.services.contract;

import com.hrm.payload.BaseResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public interface RoleService
{
    @Async
    @PreAuthorize(value = "hasAuthority('ROLE_CREATE')")
    CompletableFuture<BaseResponse> createRole(String roleName);

    @Async
    CompletableFuture<BaseResponse> createAuthority(String authorityName);

    @Async
    CompletableFuture<BaseResponse> addAuthorityToRole(Long roleId, Collection<Long> authorityIds);
}

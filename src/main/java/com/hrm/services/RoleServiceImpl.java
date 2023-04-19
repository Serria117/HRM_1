package com.hrm.services;

import com.hrm.entities.AppAuthority;
import com.hrm.entities.AppRole;
import com.hrm.payload.BaseResponse;
import com.hrm.repositories.AuthorityRepository;
import com.hrm.repositories.RoleRepository;
import com.hrm.services.contract.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;

@Service @RequiredArgsConstructor @Slf4j
@Transactional(rollbackFor = SQLException.class, timeout = 3000)
public class RoleServiceImpl implements RoleService
{
    private final RoleRepository roleRepository;
    private final AuthorityRepository authorityRepository;

    @Override
    @Async
    @PreAuthorize(value = "hasAuthority('ROLE_CREATE')")
    public CompletableFuture<BaseResponse> createRole(String roleName)
    {
        if ( !roleName.startsWith("ROLE") ) roleName = "ROLE_" + roleName;
        if ( !roleRepository.existByName(roleName) ) {
            var newRole = new AppRole().setRoleName(roleName);
            roleRepository.save(newRole);
            return CompletableFuture.completedFuture(BaseResponse.success("Role created: " + newRole.getRoleName()));
        }
        return CompletableFuture.completedFuture(BaseResponse.error("Role has already existed"));
    }

    @Override
    @Async
    public CompletableFuture<BaseResponse> createAuthority(String authorityName)
    {
        if ( !authorityRepository.existByName(authorityName) ) {
            var newAuth = new AppAuthority().setName(authorityName);
            newAuth = authorityRepository.save(newAuth);
            return CompletableFuture.completedFuture(BaseResponse.success("Authority created: " + newAuth.getName()));
        }
        return CompletableFuture.completedFuture(BaseResponse.error("Authority has already existed"));
    }

    @Override
    @Async
    public CompletableFuture<BaseResponse> addAuthorityToRole(Long roleId, Collection<Long> authorityIds)
    {
        var authorities = authorityRepository.findAllById(authorityIds);
        var role = roleRepository.findById(roleId).orElse(null);
        if ( role != null ) {
            role.setAppAuthorities(new HashSet<>(authorities));
            role = roleRepository.save(role);
            return CompletableFuture.completedFuture(BaseResponse.success(role.getRoleName() + " updated"));
        }
        return CompletableFuture.completedFuture(BaseResponse.error("Can not assign authority to role"));
    }

    public BaseResponse getAllRole(){
        try {
            var listObj = roleRepository.findAll();
            var logMessage = listObj.size() > 0
                    ? "Get list role successfully!"
                    : "List role is empty!";
            log.info(logMessage);
            return listObj.size() > 0
                    ? BaseResponse.success(listObj).setMessage("Get list role successfully!")
                    : BaseResponse.success("List role is empty!");
        } catch (Exception ex){
            log.error("Get list role fail " + ex.getMessage());
            return BaseResponse.success(ex.getMessage());
        }
    }
}

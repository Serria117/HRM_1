package com.hrm.services;

import com.hrm.dto.department.DepartmentDto;
import com.hrm.entities.AppUser;
import com.hrm.entities.Department;
import com.hrm.payload.BaseResponse;
import com.hrm.payload.DepartmentRequest;
import com.hrm.payload.DepartmentViewDto;
import com.hrm.repositories.DepartmentRepository;
import com.hrm.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service @Slf4j
@RequiredArgsConstructor
public class DepartmentServiceImpl
{
    private final static Logger LOGGER = LoggerFactory.getLogger(DepartmentServiceImpl.class);
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    @Async
    public CompletableFuture<BaseResponse> getDepartmentList(Integer page, Integer size)
    {
        var departmentList = departmentRepository.finAllNonDeleted(PageRequest.of(page, size));
        var departmentDto = departmentList.map(department -> new DepartmentDto()
                                                                     .setId(department.getId())
                                                                     .setDepartmentName(department.getDepartmentName())
                                                                     .setDepartmentCode(department.getDepartmentCode())
        );
        return CompletableFuture.completedFuture(BaseResponse.success(departmentDto));
    }

    @Transactional(rollbackFor = Exception.class)
    @Async
    public CompletableFuture<BaseResponse> createDepartment(DepartmentRequest dpmRequest,
                                                            Authentication authentication)
    {
        try {
            /*var objRequest = new DepartmentRequest()
                    .setId(dpmRequest.getId())
                    .setDepartmentName(dpmRequest.getDepartmentName())
                    .setDepartmentCode(dpmRequest.getDepartmentCode());*/

            if ( departmentRepository.existsDepartmentByName(dpmRequest.getDepartmentName()) ) {
                throw new RuntimeException("Department name already exist!");
            }

            var newDepartment = new Department()
                                        .setDepartmentName(dpmRequest.getDepartmentName())
                                        .setDepartmentCode(dpmRequest.getDepartmentCode());
            newDepartment.setCreation(authentication);

            var dpmCreate = departmentRepository.save(newDepartment);

            LOGGER.info("Create department success!");

            return CompletableFuture.completedFuture(BaseResponse.success(new DepartmentViewDto(dpmCreate)));
        }
        catch ( Exception ex ) {
            LOGGER.error("Create department fail", ex);
            return CompletableFuture.completedFuture(BaseResponse.error(ex.getMessage()));
        }
    }

    @Transactional(rollbackFor = Exception.class) @Async
    public CompletableFuture<BaseResponse> updateDepartment(DepartmentRequest dpmRequest, Authentication authentication)
    {
        try {
            var foundDep = departmentRepository.findById(dpmRequest.getId()).orElse(null);
            if ( foundDep == null ) {
                throw new RuntimeException("Department not found by id: " + dpmRequest.getId());
            }
            AppUser mngUser = null;
            if ( dpmRequest.getUserId() != null ) {mngUser = userRepository.findById(dpmRequest.getUserId()).orElse(null);}

            foundDep.setDepartmentName(dpmRequest.getDepartmentName());
            foundDep.setDepartmentCode(dpmRequest.getDepartmentCode());
            foundDep.setManagementUser(mngUser);
            foundDep.setModification(authentication);

            LOGGER.info("Update department success!");

            return CompletableFuture.completedFuture(BaseResponse.success(new DepartmentViewDto(foundDep)));
        }
        catch ( Exception ex ) {
            LOGGER.error("Update department fail!");
            return CompletableFuture.completedFuture(BaseResponse.error(ex.getMessage()));
        }
    }

    public BaseResponse changeUserManagerOfDepartment(Long dpmId, UUID mngUserId, Authentication authentication)
    {
        try {
            var dpmExist = departmentRepository.findById(dpmId).orElse(null);
            if ( dpmExist == null ) {
                throw new RuntimeException("Department not found by id: " + dpmId);
            }
            else {
                var mngUserExist = userRepository.findById(mngUserId).orElse(null);
                if ( mngUserExist == null ) {
                    throw new RuntimeException("User not found by id: " + mngUserId);
                }
                else {
                    dpmExist.setManagementUser(userRepository.getReferenceById(mngUserId));
                }
                dpmExist.setModification(authentication);
            }
            var objChange = departmentRepository.save(dpmExist);
            LOGGER.info("Change manager user of department success!");
            return BaseResponse.success(objChange);
        }
        catch ( Exception ex ) {
            LOGGER.error("Change manager user of department fail");
            return BaseResponse.error(ex.getMessage());
        }
    }

    public BaseResponse deleteDepartment(Long dpmId)
    {
        try {
            var dpmExist = departmentRepository.findById(dpmId).orElse(null);
            if ( dpmExist == null ) {
                throw new RuntimeException("Department not found by id: " + dpmId);
            }
            else {
                dpmExist.setIsDeleted(true);
            }
            var deleteDepartment = departmentRepository.save(dpmExist);
            LOGGER.info("Delete department success!");
            return BaseResponse.success(deleteDepartment);
        }
        catch ( Exception ex ) {
            LOGGER.error("Delete department by id: " + dpmId + "fail!");
            return BaseResponse.success(ex.getMessage());
        }
    }

    public BaseResponse viewDetailDepartment(Long dpmId)
    {
        try {
            var dpmExist = departmentRepository.findById(dpmId).orElse(null);
            if ( dpmExist == null ) {
                throw new RuntimeException("Department not found by id: " + dpmId);
            }
            else {
                var objEntity = departmentRepository.departmentViewDetail(dpmId);
                var objDto = new DepartmentDto()
                                     .setId(objEntity.getId())
                                     .setDepartmentName(objEntity.getDepartmentName())
                                     .setDepartmentCode(objEntity.getDepartmentCode())
                                     .setMngUser(objEntity.getMngUser())
                                     .setNumberEmployeeOfDepartment(objEntity.getNumberEmployeeOfDepartment());
                return BaseResponse.success(objDto);
            }
        }
        catch ( Exception ex ) {
            LOGGER.error(ex.getMessage());
            return BaseResponse.error(ex.getMessage());
        }
    }
}

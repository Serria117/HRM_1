package com.hrm.services;

import com.hrm.customExpeption.DuplicatedException;
import com.hrm.entities.Department;
import com.hrm.payload.BaseResponse;
import com.hrm.payload.departmentDto.DepartmentCreateDto;
import com.hrm.payload.departmentDto.DepartmentViewDto;
import com.hrm.repositories.DepartmentRepository;
import com.hrm.repositories.UserRepository;
import com.hrm.services.contract.DepartmentService;
import com.hrm.utils.CommonFn;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

@Service @RequiredArgsConstructor @Transactional(rollbackFor = Exception.class)
public class DepartmentServiceImpl implements DepartmentService
{
    private final DepartmentRepository depRepository;
    private final UserRepository userRepository;

    @Override
    public BaseResponse getListDepartment(int page, int size)
    {
        if ( page < 0 ) page = 0;
        if ( size <= 0 ) size = 200;
        var departments = depRepository.findAllPaging(PageRequest.of(page, size));
        if ( departments.getTotalElements() == 0 ) return BaseResponse.noContent();
        var res = departments.map(d -> new DepartmentViewDto().fromDepartment(d));
        return BaseResponse.success(res);
    }

    @Override
    public BaseResponse createDepartment(DepartmentCreateDto dto,
                                         Authentication auth)
    {
        try {
            if ( this.checkDepExist(dto.getDepartmentName(), dto.getDepartmentCode()) ) {
                throw new DuplicatedException("Name or code name has already been taken");
            }

            var newDepartment = new Department().setDepartmentCode(dto.getDepartmentCode())
                                                .setDepartmentName(dto.getDepartmentName());

            if ( dto.getMngUserId() != null ) {
                userRepository.findById(CommonFn.stringToUUID(dto.getMngUserId()))
                              .ifPresent(newDepartment::setManagementUser);
            }

            newDepartment.setCreation(auth);
            var savedDepartment = depRepository.save(newDepartment);
            return BaseResponse.success(new DepartmentViewDto().fromDepartment(savedDepartment)
            );
        }
        catch ( Exception e ) {
            return BaseResponse.error(e.getMessage());
        }
    }

    public BaseResponse editDepartment(Long depId, DepartmentCreateDto dto, Authentication auth)
    {
        try {
            if ( this.checkDepExist(dto.getDepartmentName(), dto.getDepartmentCode()) ) {
                throw new DuplicatedException("Name or code name has already been taken");
            }

            var foundDep = depRepository.findById(depId)
                                        .orElseThrow(() -> new NotFoundException("Department not found"));

            foundDep.setDepartmentName(dto.getDepartmentName() != null
                                       ? dto.getDepartmentName() : foundDep.getDepartmentName())
                    .setDepartmentCode(dto.getDepartmentCode() != null
                                       ? dto.getDepartmentCode() : foundDep.getDepartmentCode());

            if(dto.getMngUserId() != null) {
                userRepository.findById(CommonFn.stringToUUID(dto.getMngUserId()))
                        .ifPresent(foundDep::setManagementUser);
            }
            foundDep.setModification(auth);
            var savedDep = depRepository.save(foundDep);
            return BaseResponse.success(new DepartmentViewDto().fromDepartment(savedDep));
        }
        catch ( Exception e ) {
            return BaseResponse.error(e.getMessage());
        }
    }

    @Override
    public BaseResponse assignManager(Long depId, String userId, Authentication auth)
    {
        try {
            var foundDep = depRepository.findById(depId)
                                        .orElseThrow(()
                                                             -> new NotFoundException("Department not found"));
            var foundUser = userRepository.findById(CommonFn.stringToUUID(userId))
                                          .orElseThrow(()
                                                               -> new NotFoundException("User not found exception"));
            foundDep.setManagementUser(foundUser);
            foundDep.setModification(auth);
            return BaseResponse.success();
        }
        catch ( Exception e ) {
            return BaseResponse.error(e.getMessage());
        }
    }

    private boolean checkDepExist(String name, String code)
    {
        return depRepository.ExistByNameOrCode(name, code);
    }
}

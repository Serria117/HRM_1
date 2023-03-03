package com.hrm.services.contract;

import com.hrm.payload.BaseResponse;
import com.hrm.payload.departmentDto.DepartmentCreateDto;
import org.springframework.security.core.Authentication;

public interface DepartmentService
{
    BaseResponse getListDepartment(int page, int size);

    BaseResponse createDepartment(DepartmentCreateDto dto,
                                  Authentication auth);

    BaseResponse assignManager(Long depId, String userId, Authentication auth);
}

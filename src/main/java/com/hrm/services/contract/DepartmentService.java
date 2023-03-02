package com.hrm.services.contract;

import com.hrm.payload.BaseResponse;
import com.hrm.payload.departmentDto.DepartmentCreateDto;
import org.springframework.security.core.Authentication;
import reactor.util.annotation.Nullable;

public interface DepartmentService
{
    BaseResponse getListDepartment(int page, int size);

    BaseResponse createDepartment(DepartmentCreateDto dto,
                                  @Nullable String mngUserId,
                                  Authentication auth);

    BaseResponse assignManager(Long depId, String userId, Authentication auth);
}

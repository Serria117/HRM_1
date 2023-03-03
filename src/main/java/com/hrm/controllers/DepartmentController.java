package com.hrm.controllers;

import com.hrm.payload.BaseResponse;
import com.hrm.payload.departmentDto.DepartmentCreateDto;
import com.hrm.services.contract.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/department")
@RequiredArgsConstructor
public class DepartmentController
{
    private final DepartmentService departmentService;

    @PostMapping("create")
    public BaseResponse createDepartment(DepartmentCreateDto dto,
                                         Authentication auth)
    {
        return departmentService.createDepartment(dto, auth);
    }

    @GetMapping("get-all")
    public BaseResponse getAll(@RequestParam(required = false, defaultValue = "1") int page,
                               @RequestParam(required = false, defaultValue = "100") int size)
    {
        if ( page > 0 ) page = page - 1;
        return departmentService.getListDepartment(page, size);
    }

    @PostMapping("assign-manager")
    public BaseResponse assignManager(@RequestParam Long dId,
                                      @RequestParam String uId,
                                      Authentication auh)
    {
        return departmentService.assignManager(dId, uId, auh);
    }
}

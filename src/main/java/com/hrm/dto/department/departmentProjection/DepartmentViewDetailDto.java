package com.hrm.dto.department.departmentProjection;

import lombok.Data;

public interface DepartmentViewDetailDto {
    Long getId();
    String getDepartmentName();
    String getDepartmentCode();
    String getMngUser();
    Integer getNumberEmployeeOfDepartment();
}

package com.hrm.dto.assignment.assignmentProjection;

import com.hrm.dto.user.UserProjection.UserViewDto;

import java.util.List;

public interface AssignmentViewDto {
    Long getAsmId();
    String getTaskName();
    String getTaskDescription();
    String getAssignByUserName();
    String getNumberOfUserAssign();
    Boolean getIsActivated();
    String getHrefInfoAsm();
    String getUserInAssign();
}

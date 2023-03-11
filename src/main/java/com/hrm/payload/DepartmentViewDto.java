package com.hrm.payload;

import com.hrm.entities.Department;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * A DTO for the {@link com.hrm.entities.Department} entity
 */
@Data @AllArgsConstructor @NoArgsConstructor @Accessors(chain = true)
public class DepartmentViewDto implements Serializable
{
    private Boolean isActivated = true;
    private Boolean isDeleted = false;
    private LocalDateTime createdTime = LocalDateTime.now();
    private LocalDateTime lastModifiedTime;
    private String createdByUser;
    private String lastModifiedByUser;
    private Long id;
    @Size(max = 100) private String departmentName;
    @Size(max = 10) private String departmentCode;
    private UUID managementUserId = UUID.randomUUID();
    @Size(min = 3, max = 100) private String managementUserUsername;

    public DepartmentViewDto(Department dep){
        id = dep.getId();
        departmentName = dep.getDepartmentName();
        departmentCode = dep.getDepartmentCode();
        isActivated = dep.getIsActivated();
        createdTime = dep.getCreatedTime();
        createdByUser = dep.getCreatedByUser();
        lastModifiedTime = dep.getLastModifiedTime();
        lastModifiedByUser = dep.getLastModifiedByUser();
        managementUserId = dep.getManagementUser() == null ? null : dep.getManagementUser().getId();
        managementUserUsername = dep.getManagementUser() == null ? null : dep.getManagementUser().getUsername();

    }
}

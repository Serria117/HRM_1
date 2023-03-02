package com.hrm.payload.departmentDto;

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
    private LocalDateTime createdTime;
    private LocalDateTime lastModifiedTime;
    private String createdByUser;
    private String lastModifiedByUser;
    private Long id;
    @Size(max = 100) private String departmentName;
    @Size(max = 10) private String departmentCode;
    private UUID managementUserId = null;
    @Size(min = 3, max = 100) private String managementUserUsername;

    public DepartmentViewDto fromDepartment(Department d)
    {
        return new DepartmentViewDto()
                       .setId(d.getId())
                       .setDepartmentName(d.getDepartmentName())
                       .setDepartmentCode(d.getDepartmentCode())
                       .setCreatedTime(d.getCreatedTime())
                       .setLastModifiedByUser(d.getLastModifiedByUser())
                       .setLastModifiedTime(d.getLastModifiedTime())
                       .setCreatedByUser(d.getCreatedByUser())
                       .setManagementUserId(d.getManagementUser() == null
                                            ? null
                                            : d.getManagementUser().getId())
                       .setManagementUserUsername(d.getManagementUser() == null
                                                  ? null
                                                  : d.getManagementUser().getUsername());
    }
}

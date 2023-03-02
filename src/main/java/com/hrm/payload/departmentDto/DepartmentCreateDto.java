package com.hrm.payload.departmentDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A DTO for the {@link com.hrm.entities.Department} entity
 */
@Data @AllArgsConstructor @NoArgsConstructor @Accessors(chain = true)
public class DepartmentCreateDto implements Serializable
{
    @Size(max = 100) private String departmentName;
    @Size(max = 10) private String departmentCode;
}

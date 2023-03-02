package com.hrm.dto;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data @Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor @Accessors(chain = true)
public class DepartmentDto implements Serializable {
    private Long id;
    private String departmentName;
    private String departmentCode;
}

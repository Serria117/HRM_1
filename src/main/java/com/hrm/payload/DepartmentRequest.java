package com.hrm.payload;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.UUID;

@Data @Accessors(chain = true)
public class DepartmentRequest implements Serializable
{
    private Long id;
    private String departmentName;
    private String departmentCode;
    private UUID userId;
}

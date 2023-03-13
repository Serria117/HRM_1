package com.hrm.payload;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data @Accessors(chain = true)
public class AssignmentRequest{
    private Long id;
    private String taskName;
    private String description;
}

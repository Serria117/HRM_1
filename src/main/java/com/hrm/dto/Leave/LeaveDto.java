package com.hrm.dto.Leave;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class LeaveDto {
    private String eplName;
    private String duration;
    private String dateApply;
}
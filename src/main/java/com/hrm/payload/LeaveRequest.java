package com.hrm.payload;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.UUID;

@Data @Accessors(chain = true)
public class LeaveRequest {
    private Long leaveId;
    private UUID eplId;
    private Integer duration;
    private LocalDate dateApply;
}

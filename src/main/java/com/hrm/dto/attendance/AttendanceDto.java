package com.hrm.dto.attendance;

import lombok.Data;

@Data
public class AttendanceDto {
    private Long atdId;
    private String userName;
    private String checkInTime;
    private String checkOutTime;
    private String atdDate;
}

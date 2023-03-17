package com.hrm.dto.attendance.attendanceProjection;

public interface AttendanceViewDto {
    Long getAtdId();
    String getUserName();
    String getCheckInTime();
    String getCheckOutTime();
    String getAtdDate();
}

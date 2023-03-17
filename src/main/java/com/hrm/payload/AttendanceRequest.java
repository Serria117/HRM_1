package com.hrm.payload;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data @Accessors(chain = true)
public class AttendanceRequest {
    private Long atdId;
    private UUID userId;
    private Instant checkInTime;
    private Instant checkOutTime;
    private LocalDate atdDate;
}

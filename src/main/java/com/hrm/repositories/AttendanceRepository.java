package com.hrm.repositories;

import com.hrm.dto.attendance.attendanceProjection.AttendanceViewDto;
import com.hrm.entities.Attendance;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    @Query(nativeQuery = true, value = "select u.username as userName, atd.checkInTime as checkInTime, atd.checkOutTime as checkOutTime, atd.attendantDate as atdDate" +
            " from attendance atd" +
            " inner join user u on atd.user_id = u.id")
    public List<AttendanceViewDto> getListAttendance(Pageable pageable);
}

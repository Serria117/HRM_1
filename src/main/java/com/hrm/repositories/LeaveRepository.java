package com.hrm.repositories;

import com.hrm.dto.Leave.LeaveDto;
import com.hrm.dto.Leave.leaveProjection.LeaveProjection;
import com.hrm.entities.Leave;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LeaveRepository extends JpaRepository<Leave, Long> {

    @Query(nativeQuery = true, value = "select u.username as eplName, " +
            "       (CASE" +
            "            WHEN (lv.duration = 0) THEN N'Cả ngày' " +
            "            ELSE N'Nửa ngày'" +
            "        END) as duration, lv.dateApply as dateApply " +
            " from leave_record lv" +
            " inner join user u on u.id = lv.registerEmployee")
    Optional<List<LeaveProjection>> getAllLeaveNonDeleted(Pageable pageable);
    @Query(nativeQuery = true, value = " select * from leave_record where registerEmployee = ?1 and dateApply = ?2")
    Optional<LeaveProjection> findCurrentLeaveByDateApply(UUID eplId, LocalDate dateApply);
}

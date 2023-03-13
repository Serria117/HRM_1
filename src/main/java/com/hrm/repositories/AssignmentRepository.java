package com.hrm.repositories;

import com.hrm.dto.assignment.AssignmentDto;
import com.hrm.entities.Assignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    @Query(nativeQuery = true, value = "select * from assignment where isDeleted = false order by createdTime")
    Page<AssignmentDto> getAllAssignmentNonDeleted(Pageable pageable);

    @Query(nativeQuery = true, value = "select * from Assignment asm where asm.taskName = ?1")
    List<Assignment> findAssignmentByTaskName(String taskName);

}

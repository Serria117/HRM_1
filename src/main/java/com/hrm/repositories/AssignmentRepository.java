package com.hrm.repositories;

import com.hrm.dto.assignment.AssignmentDto;
import com.hrm.dto.assignment.assignmentProjection.AssignmentViewDto;
import com.hrm.dto.user.UserProjection.UserViewDto;
import com.hrm.entities.Assignment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    @Query(nativeQuery = true, value = " select  asm.id as asmId, asm.taskName as taskName, asm.createdByUser as assignByUserName,\n" +
            "        asm.taskDescription as taskDescription , asm.isActivated as isActivated,\n" +
            "        count(tu.assign_by_id) as numberOfUserAssign, asm.taskName as hrefInfoAsm\n" +
            "from assignment asm\n" +
            "left join task_user tu on asm.id = tu.task_id\n" +
            "where asm.isDeleted = false\n" +
            "group by asm.id ")
    Page<AssignmentViewDto> getAllAssignmentNonDeleted(Pageable pageable);

    @Query(nativeQuery = true, value = "select * from Assignment asm where asm.taskName = ?1")
    List<Assignment> findAssignmentByTaskName(String taskName);

    List<Assignment> findAssignmentsByTaskName(String taskName);


    @Query(nativeQuery = true, value = "select BIN_TO_UUID(u.id) as id, u.username as username, u.fullName as fullName,\n" +
            "u.email as email, u.phone as phone,\n" +
            "u.address as address, u.bankAccount as bankAccount, u.bankFullName as bankFullName, u.bankShortName as bankShortName\n" +
            "from task_user tu\n" +
            "inner join assignment a on tu.task_id = a.id\n" +
            "inner join user u on tu.assign_by_id = u.id\n" +
            "where a.id = ?1")
    List<UserViewDto> getListUserInAsm(Long asmId);

    @Query(nativeQuery = true, value = "select *\n" +
            "from task_user tu\n" +
            "inner join assignment a on tu.task_id = a.id\n" +
            "inner join user u on tu.assign_by_id = u.id\n" +
            "where a.id = ?1")
    List<AssignmentDto> getListUserInAsm2(Long asmId);

}

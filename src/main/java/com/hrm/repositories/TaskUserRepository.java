package com.hrm.repositories;

import com.hrm.dto.taskuser.taskUserProjection.TaskUserViewDto;
import com.hrm.entities.Assignment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TaskUserRepository extends JpaRepository<Assignment, Long>
{

    @Query(nativeQuery = true,
            value = "select tu.task_id as taskId, " +
                            "a.taskName as taskName, " +
                            "BIN_TO_UUID(tu.assign_by_id) as assignById, " +
                            "u.username as assignByName " +
                            "from task_user tu " +
                            " inner join assignment a on tu.task_id = a.id " +
                            " inner join user u on tu.assign_by_id = u.id " +
                            " order by tu.task_id")
    List<TaskUserViewDto> listTaskUser(Pageable pageable);

    @Query(nativeQuery = true,
            value = "select tu.task_id as taskId, a.taskName as taskName, tu.assign_by_id as assignById, u.username as assignByName from task_user tu " +
                            " inner join assignment a on tu.task_id = a.id " +
                            " inner join user u on tu.assign_by_id = u.id " +
                            " where tu.task_id = ?1")
    List<TaskUserViewDto> userListOfAssignment(Long asmId, Pageable pageable);

    @Modifying
    @Query(nativeQuery = true,
            value = "INSERT INTO task_user (task_id, assign_by_id) VALUES (?1, ?2)")
    void createUserOfTask(Long task_id, UUID assign_by_id);

    @Query(nativeQuery = true,
            value = "select tu.task_id as taskId, a.taskName as taskName, tu.assign_by_id as assignById, u.username as assignByName from task_user tu " +
                            " inner join assignment a on tu.task_id = a.id " +
                            " inner join user u on tu.assign_by_id = u.id " +
                            " where tu.task_id = ?1 and tu.assign_by_id = ?2")
    List<TaskUserViewDto> findCurrentUserOfTask(Long task_id, UUID assign_by_id);
}

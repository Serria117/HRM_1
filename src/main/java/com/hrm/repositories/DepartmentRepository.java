package com.hrm.repositories;

import com.hrm.entities.Department;
import com.hrm.payload.departmentDto.DepartmentViewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long>
{
    @Query("select d from Department d order by d.departmentName")
    Page<Department> findAllPaging(Pageable pageable);

    @Query("""
            select (count(d) > 0) from Department d
            where upper(d.departmentName) = upper(?1) and upper(d.departmentCode) = upper(?2)""")
    boolean ExistByNameOrCode(String departmentName, String departmentCode);

    @Query(nativeQuery = true,
    value = "SELECT dp.id as id, dp.departmentName as name, u.username as username " +
                    "FROM department as dp INNER JOIN user as u " +
                    "WHERE dp.mng_user_id = u.id")
    List<DepartmentViewDto> viewAllAsDto();

}

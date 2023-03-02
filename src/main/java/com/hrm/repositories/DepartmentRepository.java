package com.hrm.repositories;

import com.hrm.entities.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    @Query("select dpmt from Department dpmt where dpmt.isDeleted = false order by dpmt.createdTime, dpmt.managementUser.username")
    /*@Query("select dpmt from Department dpmt inner join AppUser u on dpmt.managementUser.id = u.id where dpmt.isDeleted = false order by dpmt.createdTime, dpmt.managementUser.username")*/
    Page<Department> finAllNonDeleted(Pageable pageable);

    @Query("select (count(dpm) > 0) from Department dpm where dpm.departmentName = ?1")
    boolean existsDepartmentByName(String dpmName);
}

package com.hrm.repositories;

import com.hrm.dto.department.departmentProjection.DepartmentViewDetailDto;
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
    @Query(nativeQuery = true, value = "select dpm.id as id," +
            "       dpm.departmentName as departmentName," +
            "       dpm.departmentCode as departmentCode," +
            "       u.username as mngUser," +
            "       count(dpm.mng_user_id) as numberEmployeeOfDepartment" +
            "       from department as dpm" +
            "       left join user as u on dpm.mng_user_id = u.id" +
            "       where dpm.isActivated = true" +
            "       and dpm.isDeleted = false" +
            "       and dpm.id = ?1" +
            "       group by dpm.departmentName")
    DepartmentViewDetailDto departmentViewDetail(Long dpmId);
}
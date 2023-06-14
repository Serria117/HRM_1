package com.hrm.repositories;

import com.hrm.dto.department.departmentProjection.DepartmentViewDetailDto;
import com.hrm.dto.user.UserProjection.UserViewDto;
import com.hrm.entities.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    @Query("select dpmt from Department dpmt where dpmt.isDeleted = false order by dpmt.createdTime")
    Page<Department> finAllNonDeleted(Pageable pageable);

    @Query(value = "select (count(dpm) > 0) from Department dpm where dpm.departmentName = ?1")
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

    @Query(nativeQuery = true, value = "select u.id as id, u.username as userName, u.fullName as fullName, u.email as email, u.phone as phone " +
            " from department d " +
            " inner join user u on d.mng_user_id = u.id " +
            " where d.id = ?1")
    List<UserViewDto> getListUserDpm(Long dpmId);

    @Query(nativeQuery = true, value = "select dpm.id as id," +
            "       dpm.departmentName as departmentName," +
            "       dpm.departmentCode as departmentCode," +
            "       u.username as mngUser," +
            "       count(dpm.mng_user_id) as numberEmployeeOfDepartment" +
            "       from department as dpm" +
            "       left join user as u on dpm.mng_user_id = u.id" +
            "       where dpm.isActivated = true" +
            "       and dpm.isDeleted = false" +
            "       group by dpm.id asc")
    List<DepartmentViewDetailDto> lstDepartmentViewDetail(Pageable pageable);
}

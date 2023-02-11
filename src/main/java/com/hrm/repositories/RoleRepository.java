package com.hrm.repositories;

import com.hrm.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface RoleRepository extends JpaRepository<AppRole, Long>
{
    @Query("select a from AppRole a where upper(a.roleName) = upper(?1)")
    AppRole findByName(String roleName);

    @Query("select a from AppRole a where upper(a.roleName) = upper(?1)")
    Collection<AppRole> findAllByNames(String roleName);

    @Query("select (count(a) > 0) from AppRole a where a.roleName = ?1")
    boolean existByName(String roleName);
}

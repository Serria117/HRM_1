package com.hrm.repositories;

import com.hrm.entities.AppAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AuthorityRepository extends JpaRepository<AppAuthority, Long>
{
    @Query("select (count(a) > 0) from AppAuthority a where a.name = ?1")
    boolean existByName(String name);

}

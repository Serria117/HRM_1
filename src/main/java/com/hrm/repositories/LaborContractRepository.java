package com.hrm.repositories;

import com.hrm.entities.LaborContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LaborContractRepository extends JpaRepository<LaborContract, Long>
{
    @Query(nativeQuery = true,
    value = "select * from labor_contract l " +
                    "inner join user u on l.user_id = u.id" +
                    "where u.username = ?1 and l.is_activated = ?2")
    Optional<LaborContract> findByCurrentContract(String username, Boolean isActivated);

}

package com.hrm.repositories;

import com.hrm.entities.LaborContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface LaborContractRepository extends JpaRepository<LaborContract, Long>
{
    @Query(nativeQuery = true,
    value = "select * from labor_contract l " +
                    "inner join user u on l.user_id = u.id" +
                    "where u.id = ?1 and l.is_activated = ?2")
    Optional<LaborContract> findByCurrentContract(UUID userId, Boolean isActivated);


}

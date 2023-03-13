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
                    "where u.id = ?1 and l.isActivated = ?2")
    Optional<LaborContract> findByCurrentContract(UUID userId, Boolean isActivated);

    @Query(nativeQuery = true, value = "select l.id as lbId, u.username as lbUser, l.contractNumber as lbNumber, ct.typeName as lbTypeName," +
            "       u.email as lbUserEmail, u.phone as lbUserPhone, l.startDate as startDate, l.endDate as endDate" +
            " from labor_contract as l" +
            "    inner join user as u on l.userId = u.id" +
            "    inner join contract_type as ct on l.contractTypeId = ct.id" +
            " where l.isDeleted = false")
    Page<LaborContractViewDetailDto> getAllContractNonDeleted(Pageable pageable);
}

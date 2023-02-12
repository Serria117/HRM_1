package com.hrm.repositories;

import com.hrm.entities.ContractType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ContractTypeRepository extends JpaRepository<ContractType, Long>
{
    @Query("select (count(c) > 0) from ContractType c where upper(c.typeName) = upper(?1)")
    boolean existByName(String typeName);

}

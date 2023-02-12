package com.hrm.services;

import com.hrm.entities.LaborContract;
import com.hrm.repositories.ContractTypeRepository;
import com.hrm.repositories.LaborContractRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service @Slf4j @RequiredArgsConstructor
public class ContractServiceImpl
{
    private final LaborContractRepository laborContractRepository;
    private final ContractTypeRepository contractTypeRepository;

    @Transactional(rollbackFor = Exception.class)
    public LaborContract createNewContract(UUID userId,
                                           Long contractTypeId,
                                           Authentication authentication)
    {
        var newContract = new LaborContract().setUserId(userId)
                                             .setContractTypeId(contractTypeId);
        newContract.setCreation(authentication);

        return laborContractRepository.save(newContract);
    }
}

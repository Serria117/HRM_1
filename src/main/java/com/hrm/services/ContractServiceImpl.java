package com.hrm.services;

import com.hrm.entities.LaborContract;
import com.hrm.payload.BaseResponse;
import com.hrm.repositories.ContractTypeRepository;
import com.hrm.repositories.LaborContractRepository;
import com.hrm.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.util.annotation.Nullable;

import java.time.LocalDate;
import java.util.UUID;

@Service @Slf4j @RequiredArgsConstructor
public class ContractServiceImpl
{
    private final LaborContractRepository laborContractRepository;
    private final ContractTypeRepository contractTypeRepository;
    private final UserRepository userRepository;

    @Transactional(rollbackFor = Exception.class)
    public BaseResponse createNewContract(UUID userId,
                                          Long contractTypeId,
                                          LocalDate startDate,
                                          @Nullable LocalDate endDate,
                                          Authentication authentication)
    {
        if ( !authentication.isAuthenticated() ) return BaseResponse.error("Unauthorized");
        //check valid user:
        if ( !userRepository.existsById(userId) ) {
            return BaseResponse.error("Invalid User Id");
        }

        laborContractRepository.findByCurrentContract(userId, true)
                               .ifPresent(c -> c.setIsActivated(false));

        var newContract = new LaborContract().setUserId(userId)
                                             .setStartDate(startDate)
                                             .setEndDate(endDate)
                                             .setContractTypeId(contractTypeId);
        newContract.setCreation(authentication);
        return BaseResponse.success(laborContractRepository.save(newContract));
    }

}

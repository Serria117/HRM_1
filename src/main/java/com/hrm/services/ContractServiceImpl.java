package com.hrm.services;

import com.baseapp.services.contract.ContractService;
import com.hrm.entities.LaborContract;
import com.hrm.payload.BaseResponse;
import com.hrm.repositories.ContractTypeRepository;
import com.hrm.repositories.LaborContractRepository;
import com.hrm.repositories.UserRepository;
import com.hrm.utils.CommonFn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.util.annotation.Nullable;

import java.time.LocalDate;

@Service @Slf4j @RequiredArgsConstructor
public class ContractServiceImpl implements ContractService
{
    private final LaborContractRepository laborContractRepository;
    private final ContractTypeRepository contractTypeRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResponse createNewContract(String userId,
                                          Long contractTypeId,
                                          LocalDate startDate,
                                          @Nullable LocalDate endDate,
                                          Authentication authentication)
    {
        if ( !authentication.isAuthenticated() ) return BaseResponse.error("Unauthorized");
        //check valid user:
        var uId = CommonFn.stringToUUID(userId);
        if ( !userRepository.existsById(uId) ) {
            return BaseResponse.error("Invalid User Id");
        }

        laborContractRepository.findByCurrentContract(uId, true)
                               .ifPresent(c -> c.setIsActivated(false));

        var newContract = new LaborContract().setUserId(uId)
                                             .setStartDate(startDate)
                                             .setEndDate(endDate)
                                             .setContractTypeId(contractTypeId);
        newContract.setCreation(authentication);
        return BaseResponse.success(laborContractRepository.save(newContract));
    }

}

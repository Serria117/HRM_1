package com.baseapp.services.contract;

import com.hrm.payload.BaseResponse;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import reactor.util.annotation.Nullable;

import java.time.LocalDate;

public interface ContractService
{
    @Transactional(rollbackFor = Exception.class)
    BaseResponse createNewContract(String userId,
                                   Long contractTypeId,
                                   LocalDate startDate,
                                   @Nullable LocalDate endDate,
                                   Authentication authentication);
}

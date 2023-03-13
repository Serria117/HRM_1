package com.hrm.services;

import com.hrm.dto.LaborContractDto;
import com.hrm.entities.LaborContract;
import com.hrm.payload.BaseResponse;
import com.hrm.payload.LaborContractRequest;
import com.hrm.repositories.ContractTypeRepository;
import com.hrm.repositories.LaborContractRepository;
import com.hrm.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Slf4j
@RequiredArgsConstructor
public class ContractServiceImpl
{
    private final static Logger LOGGER = LoggerFactory.getLogger(ContractServiceImpl.class);
    private final LaborContractRepository laborContractRepository;
    private final ContractTypeRepository contractTypeRepository;
    private final AppUserServiceImpl userService;
    private final UserRepository userRepository;

    @Transactional(rollbackFor = Exception.class)
    public BaseResponse createNewContract(LaborContractRequest lbContractRequest,
                                          Authentication authentication)
    {
        try {
            if ( !userRepository.existsById(lbContractRequest.getUserId()) ) {throw new RuntimeException("User id not found");}

            Example<LaborContract> exampleContract =
                    Example.of((LaborContract) new LaborContract()
                                                       .setUserId(lbContractRequest.getUserId())
                                                       .setIsActivated(true));
            laborContractRepository.findOne(exampleContract)
                                   .ifPresent(c -> c.setIsActivated(false));

            var newContract = new LaborContract()
                                      .setUserId(lbContractRequest.getUserId())
                                      .setContractNumber(lbContractRequest.getContractNumber())
                                      .setUserId(lbContractRequest.getUserId())
                                      .setContractTypeId(lbContractRequest.getContractTypeId())
                                      .setBasicSalary(lbContractRequest.getBasicSalary())
                                      .setStartDate(lbContractRequest.getStartDate())
                                      .setEndDate(lbContractRequest.getEndDate());
            newContract.setCreation(authentication);

            var saved = laborContractRepository.save(newContract);
            return BaseResponse.success(saved);
        }
        catch ( Exception e ) {
            return BaseResponse.error(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseResponse updateContract(LaborContractRequest request,
                                       Authentication authentication)
    {
        try {
            var foundContract = laborContractRepository
                                        .findById(request.getId())
                                        .orElseThrow(() -> new RuntimeException("Invalid contract Id"));
            foundContract.setBasicSalary(request.getBasicSalary())
                         .setEndDate(request.getEndDate())
                         .setIsActivated(request.getIsActivated());
            foundContract.setModification(authentication);
            laborContractRepository.save(foundContract);
            return BaseResponse.success();
        }
        catch ( Exception ex ) {
            LOGGER.error("Change status contract fail!", ex);
            return BaseResponse.error(ex.getMessage());
        }
    }

    public BaseResponse contractViewDetail(Long lbContractId)
    {
        try {
            var foundContract = laborContractRepository
                                        .findById(lbContractId)
                                        .orElseThrow(() -> new RuntimeException("Invalid contract Id"));
            var user = userRepository
                               .findById(foundContract.getUserId())
                               .orElseThrow(() -> new RuntimeException("Invalid employee"));
            var contractType = contractTypeRepository
                                       .findById(foundContract.getContractTypeId())
                                       .orElseThrow(() -> new RuntimeException("Invalid contract type"));
            var contractDto =
                    new LaborContractDto().setId(foundContract.getId())
                                          .setContractNumber(foundContract.getContractNumber())
                                          .setFullName(user.getFullName())
                                          .setContractTypeName(contractType.getTypeName())
                                          .setEmail(user.getEmail())
                                          .setPhone(user.getPhone())
                                          .setStartDate(foundContract.getStartDate())
                                          .setEndDate(foundContract.getEndDate());
            return BaseResponse.success(contractDto);
        }
        catch ( Exception ex ) {
            LOGGER.error("Contract view detail fail:", ex);
            return BaseResponse.error(ex.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseResponse deleteContract(Long id)
    {
        try {
            var foundContract = laborContractRepository.findById(id)
                                        .orElseThrow(() -> new RuntimeException("Invalid contract Id"));
            foundContract.setIsActivated(false);
            foundContract.setIsDeleted(true);

            return BaseResponse.success(foundContract);
        }
        catch ( Exception e ) {
            return BaseResponse.error(e.getMessage());
        }
    }
}

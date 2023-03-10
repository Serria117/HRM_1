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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

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
    public LaborContract createNewContract(UUID userId,
                                           Long contractTypeId,
                                           Authentication authentication)
    {
        var newContract = new LaborContract().setUserId(userId)
                                             .setContractTypeId(contractTypeId);
        newContract.setCreation(authentication);

        return laborContractRepository.save(newContract);
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseResponse createNewContract(LaborContractRequest lbContractRequest,
                                          Authentication authentication)
    {
        try {
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
    public BaseResponse createNewContractOfUser(LaborContractRequest lbContractRequest, Authentication authentication)
    {
        try {
            var userExist = userRepository.findById(lbContractRequest.getUserId())
                                          .orElseThrow(() -> new RuntimeException("User not found by id: " + lbContractRequest.getUserId()));
            var lbCurrentContract = laborContractRepository.findByCurrentContract(lbContractRequest.getUserId(), true).orElse(null);
            if ( lbCurrentContract != null ) {
                lbCurrentContract.setIsActivated(false);
                laborContractRepository.save(lbCurrentContract);
            }

            var newContract = new LaborContract()
                                      .setId(lbContractRequest.getId())
                                      .setContractNumber(lbContractRequest.getContractNumber())
                                      .setUserId(lbContractRequest.getUserId())
                                      .setContractTypeId(lbContractRequest.getContractTypeId())
                                      .setBasicSalary(lbContractRequest.getBasicSalary())
                                      .setStartDate(lbContractRequest.getStartDate())
                                      .setEndDate(lbContractRequest.getEndDate());
            newContract.setCreation(authentication);

            var savedContract = laborContractRepository.save(newContract);
            LOGGER.info("Create contract success!");
            return BaseResponse.success(savedContract);
        }
        catch ( Exception ex ) {
            LOGGER.error("Create contractOfUser fail", ex);
            return BaseResponse.error(ex.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseResponse updateContract(LaborContractRequest request, Authentication authentication)
    {
        try {
            var foundContract = laborContractRepository.findById(request.getId())
                                                       .orElseThrow(() -> new RuntimeException("Invalid contract Id"));
            foundContract.setBasicSalary(request.getBasicSalary())
                         .setEndDate(request.getEndDate());

            var savedContract = laborContractRepository.save(foundContract);
            return BaseResponse.success("Contract [" + savedContract.getContractNumber() + "] updated successfully");
        }
        catch ( Exception ex ) {
            LOGGER.error("Update contract of user fail", ex);
            return BaseResponse.error(ex.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseResponse changeStatusContract(Long lbContactId, Authentication authentication)
    {
        try {
            var foundContract = laborContractRepository.findById(lbContactId)
                                                       .orElseThrow(() -> new RuntimeException("Invalid contract Id"));
            foundContract.setIsActivated(false);
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
            var user = userRepository.getReferenceById(foundContract.getUserId());

            var contractDto =
                    new LaborContractDto().setId(foundContract.getId())
                                          .setContractNumber(foundContract.getContractNumber())
                                          .setContractOfUser(user.getFullName())
                                          .setContractTypeName(contractTypeRepository
                                                                       .getReferenceById(foundContract.getContractTypeId())
                                                                       .getTypeName())
                                          .setEmailOfUser(user.getEmail())
                                          .setPhoneOfUser(user.getPhone())
                                          .setStartDate(foundContract.getStartDate())
                                          .setEndDate(foundContract.getEndDate());
            return BaseResponse.success(contractDto);
        }
        catch ( Exception ex ) {
            LOGGER.error("Contract view detail fail:", ex);
            return BaseResponse.error(ex.getMessage());
        }
    }
}

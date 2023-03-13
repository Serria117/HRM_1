package com.hrm.services;

import com.hrm.dto.laborContract.LaborContractDto;
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
import org.springframework.data.domain.PageRequest;
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

    public BaseResponse getListLbContract(Integer page, Integer size){
        var lbContract = laborContractRepository.getAllContractNonDeleted(PageRequest.of(page, size));
        return BaseResponse.success(lbContract);
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
            var userExist = userRepository.findById(lbContractRequest.getUserId()).orElse(null);
            if ( userExist == null ) {
                throw new RuntimeException("User not found by id: " + lbContractRequest.getUserId());
            }
            else {
                var lbCurrentContract = laborContractRepository.findByCurrentContract(lbContractRequest.getUserId(), true)
                        .orElse(null);
                if ( lbCurrentContract != null ) {
                    lbCurrentContract.setIsActivated(false);
                    laborContractRepository.save(lbCurrentContract);
                }
            }

            var newContractOfUser = new LaborContract()
                                            .setId(lbContractRequest.getId())
                                            .setContractNumber(lbContractRequest.getContractNumber())
                                            .setUserId(lbContractRequest.getUserId())
                                            .setContractTypeId(lbContractRequest.getContractTypeId())
                                            .setBasicSalary(lbContractRequest.getBasicSalary())
                                            .setStartDate(lbContractRequest.getStartDate())
                                            .setEndDate(lbContractRequest.getEndDate());
            newContractOfUser.setCreation(authentication);

            var contractCreate = laborContractRepository.save(newContractOfUser);
            LOGGER.info("Create contract success!");
            return BaseResponse.success(contractCreate);
        }
        catch ( Exception ex ) {
            LOGGER.error("Create contractOfUser fail", ex);
            return BaseResponse.error(ex.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseResponse updateContract(Long lbContractId, Double baseSalary, Authentication authentication)
    {
        try {
            var objExits = laborContractRepository.findById(lbContractId)
                    .orElseThrow(() -> new RuntimeException("Invalid contract id!"));

            objExits.setId(lbContractId);
            objExits.setBasicSalary(baseSalary);
            objExits.setModification(authentication);

            laborContractRepository.save(objExits);
            LOGGER.info("Update contract successfully!");
            return BaseResponse.success();
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

    @Transactional(rollbackFor = Exception.class)
    public BaseResponse deleteContract(Long lbContractId, Authentication authentication){
        try {
            var foundContract = laborContractRepository.findById(lbContractId)
                    .orElseThrow(() -> new RuntimeException("Invalid contract Id"));

            foundContract.setIsDeleted(true);
            foundContract.setModification(authentication);

            laborContractRepository.save(foundContract);
            LOGGER.info("Delete contract successfully!");

            return BaseResponse.success();
        } catch (Exception ex){
            LOGGER.error("Delete contract fail!");
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
                    new LaborContractDto().setLbId(foundContract.getId())
                                          .setLbNumber(foundContract.getContractNumber())
                                          .setLbUserName(user.getFullName())
                                          .setLbTypeName(contractTypeRepository
                                                                       .getReferenceById(foundContract.getContractTypeId())
                                                                       .getTypeName())
                                          .setLbUserEmail(user.getEmail())
                                          .setLbUserPhone(user.getPhone())
                                          .setStartDate(foundContract.getStartDate())
                                          .setEndDate(foundContract.getEndDate());
            return BaseResponse.success(contractDto);
        }
        catch ( Exception ex ) {
            LOGGER.error("Contract view detail fail:", ex);
            return BaseResponse.error(ex.getMessage());
        }
    }

    public BaseResponse testCurrentContract(UUID userId){
        var res = laborContractRepository.findByCurrentContract(userId, true);
        return BaseResponse.success(res);
    }
}

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
    public LaborContract createNewContractt(LaborContractRequest lbContractRequest,
                                           Authentication authentication)
    {
        var newContract = new LaborContract()
                .setUserId(lbContractRequest.getUserId())
                .setContractNumber(lbContractRequest.getContractNumber())
                .setUserId(lbContractRequest.getUserId())
                .setContractTypeId(lbContractRequest.getContractTypeId())
                .setBasicSalary(lbContractRequest.getBasicSalary())
                .setStartDate(lbContractRequest.getStartDate())
                .setEndDate(lbContractRequest.getEndDate());
        newContract.setCreation(authentication);

        return laborContractRepository.save(newContract);
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseResponse createNewContractOfUser(LaborContractRequest lbContractRequest, Authentication authentication){
        try {
            var userExist = userRepository.findById(lbContractRequest.getUserId()).orElse(null);
            if (userExist == null) {
                throw new RuntimeException("User not found by id: " + lbContractRequest.getUserId());
            } else {
                var lbCurrentContract = laborContractRepository.findByCurrentContract(lbContractRequest.getUserId(), true).orElse(null);
                if (lbCurrentContract != null ){
                    lbCurrentContract.setIsActivated(false);
                    laborContractRepository.save(lbCurrentContract);
                }
                /*laborContractRepository.findByCurrentContract(lbContractRequest.getUserId(), true).ifPresent(s -> {
                    s.setIsActivated(false);
                    laborContractRepository.save(s);
                });*/
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
        } catch (Exception ex){
            LOGGER.error("Create contractOfUser fail", ex);
            return BaseResponse.error(ex.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateContractOfUser(Long lbContractId, Double baseSalary, Authentication authentication){
        try {
            var objExits = laborContractRepository.findById(lbContractId);
            if (objExits.isEmpty()){
                LOGGER.warn("Contract does not exist!");
            } else {
                var objRequest = new LaborContractRequest()
                    .setId(objExits.get().getId())
                    .setBasicSalary(baseSalary);

                var objData = new LaborContract()
                        .setId(objRequest.getId())
                        .setBasicSalary(objRequest.getBasicSalary());
                objData.setModification(authentication);

                laborContractRepository.save(objData);
            }
        } catch (Exception ex){
            LOGGER.error("Update contract of user fail", ex);
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    public void changeStatusContract(Long lbContactId, Authentication authentication){
        try {
            if (!laborContractRepository.existsById(lbContactId)) {
                LOGGER.warn("Contact does not exist!");
            } else {
                laborContractRepository.findById(lbContactId).ifPresent(s -> {
                    s.setIsActivated(false);
                    s.setModification(authentication);
                    laborContractRepository.save(s);
                });
            }
        } catch (Exception ex){
            LOGGER.error("Change status contract fail!", ex);
        }
    }

    public LaborContractDto contractViewDetail(Long lbContractId){
        LaborContractDto contractDto = null;
        var entity = laborContractRepository.findById(lbContractId);
        try {

            if (entity.isEmpty()) {
                LOGGER.warn("Contact does not exist!");
            } else {
                contractDto = new LaborContractDto();
                contractDto.setId(entity.get().getId());
                contractDto.setContractNumber(entity.get().getContractNumber());
                contractDto.setContractOfUser(userRepository.getReferenceById(entity.get().getUserId()).getFullName());
                contractDto.setContractTypeName(contractTypeRepository.getReferenceById(entity.get().getContractTypeId()).getTypeName());
                contractDto.setEmailOfUser(userRepository.getReferenceById(entity.get().getUserId()).getEmail());
                contractDto.setPhoneOfUser(userRepository.getReferenceById(entity.get().getUserId()).getPhone());
                contractDto.setStartDate(entity.get().getStartDate());
                contractDto.setEndDate(entity.get().getEndDate());
            }
        } catch (Exception ex) {
            LOGGER.error("Contract view detail fail:", ex);
        }

        return contractDto;
    }
}

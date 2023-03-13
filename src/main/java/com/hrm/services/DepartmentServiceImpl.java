package com.hrm.services;

import com.hrm.dto.department.DepartmentDto;
import com.hrm.entities.Department;
import com.hrm.payload.BaseResponse;
import com.hrm.payload.DepartmentRequest;
import com.hrm.repositories.DepartmentRepository;
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
public class DepartmentServiceImpl {
    private final static Logger LOGGER = LoggerFactory.getLogger(DepartmentServiceImpl.class);
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    public BaseResponse getDepartmentList(Integer page, Integer size){
        var departmentList = departmentRepository.finAllNonDeleted(PageRequest.of(page, size));
        var departmentDto = departmentList.map(department -> new DepartmentDto()
                .setId(department.getId())
                .setDepartmentName(department.getDepartmentName())
                .setDepartmentCode(department.getDepartmentCode())
        );
        return BaseResponse.success(departmentDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseResponse createDepartment(DepartmentRequest dpmRequest, Authentication authentication){
        try {
            /*var objRequest = new DepartmentRequest()
                    .setId(dpmRequest.getId())
                    .setDepartmentName(dpmRequest.getDepartmentName())
                    .setDepartmentCode(dpmRequest.getDepartmentCode());*/

            if (!departmentRepository.existsDepartmentByName(dpmRequest.getDepartmentName()))
                throw new RuntimeException("Department name already exist!");

            var objData = new Department()
                    .setId(dpmRequest.getId())
                    .setDepartmentName(dpmRequest.getDepartmentName())
                    .setDepartmentCode(dpmRequest.getDepartmentCode());
            objData.setCreation(authentication);

            var dpmCreate = departmentRepository.save(objData);

            LOGGER.info("Create department success!");

            return BaseResponse.success(dpmCreate);
        } catch (Exception ex){
            LOGGER.error("Create department fail", ex);
            return BaseResponse.error(ex.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseResponse updateDepartment(DepartmentRequest dpmRequest, Authentication authentication){
        try {
            var dpmExist = departmentRepository.findById(dpmRequest.getId()).orElse(null);
            var userExist = userRepository.findById(dpmRequest.getUserId()).orElse(null);
            if (dpmExist == null) {
                throw new RuntimeException("Department not found by id: " + dpmRequest.getId());
            }
            if (userExist == null) {
                throw new RuntimeException("User not found by id: " + dpmRequest.getUserId());
            } else {
                dpmExist.setManagementUser(userExist)
                        .setDepartmentName(dpmRequest.getDepartmentName())
                        .setDepartmentCode(dpmRequest.getDepartmentCode())
                        .setModification(authentication);
            }

            var dpmUpdate = departmentRepository.save(dpmExist);
            LOGGER.info("Update department success!");

            return BaseResponse.success(dpmUpdate);
        } catch (Exception ex){
            LOGGER.error("Update department fail!");
            return BaseResponse.error(ex.getMessage());
        }
    }

    public BaseResponse changeUserManagerOfDepartment(Long dpmId, UUID mngUserId, Authentication authentication) {
        try {
            var dpmExist = departmentRepository.findById(dpmId).orElse(null);
            if (dpmExist == null) {
                throw new RuntimeException("Department not found by id: " + dpmId);
            } else {
                var mngUserExist = userRepository.findById(mngUserId).orElse(null);
                if (mngUserExist == null){
                    throw new RuntimeException("User not found by id: " + mngUserId);
                } else {
                    dpmExist.setManagementUser(userRepository.getReferenceById(mngUserId));
                }
                dpmExist.setModification(authentication);
            }
            var objChange = departmentRepository.save(dpmExist);
            LOGGER.info("Change manager user of department success!");
            return BaseResponse.success(objChange);
        } catch (Exception ex){
            LOGGER.error("Change manager user of department fail");
            return BaseResponse.error(ex.getMessage());
        }
    }

    public BaseResponse deleteDepartment(Long dpmId){
        try {
            var dpmExist = departmentRepository.findById(dpmId).orElse(null);
            if (dpmExist == null){
                throw new RuntimeException("Department not found by id: " + dpmId);
            } else {
                dpmExist.setIsDeleted(true);
            }
            var deleteDepartment = departmentRepository.save(dpmExist);
            LOGGER.info("Delete department success!");
            return BaseResponse.success(deleteDepartment);
        } catch (Exception ex){
            LOGGER.error("Delete department by id: " + dpmId + "fail!");
            return BaseResponse.success(ex.getMessage());
        }
    }

    public BaseResponse viewDetailDepartment(Long dpmId){
        try {
            var dpmExist = departmentRepository.findById(dpmId).orElse(null);
            if (dpmExist == null){
                throw new RuntimeException("Department not found by id: " + dpmId);
            } else {
                var objEntity = departmentRepository.departmentViewDetail(dpmId);
                var objDto = new DepartmentDto()
                        .setId(objEntity.getId())
                        .setDepartmentName(objEntity.getDepartmentName())
                        .setDepartmentCode(objEntity.getDepartmentCode())
                        .setMngUser(objEntity.getMngUser())
                        .setNumberEmployeeOfDepartment(objEntity.getNumberEmployeeOfDepartment());
                return BaseResponse.success(objDto);
            }
        } catch (Exception ex){
            LOGGER.error(ex.getMessage());
            return BaseResponse.error(ex.getMessage());
        }
    }
}

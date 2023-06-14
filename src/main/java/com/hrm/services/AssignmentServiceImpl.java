package com.hrm.services;

import com.hrm.customExpeption.IdNotFoundException;
import com.hrm.dto.assignment.AssignmentDto;
import com.hrm.entities.Assignment;
import com.hrm.payload.AssignmentRequest;
import com.hrm.payload.BaseResponse;
import com.hrm.repositories.AssignmentRepository;
import com.hrm.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.Objects;

@Service @Slf4j
@RequiredArgsConstructor
public class AssignmentServiceImpl {
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;

    public BaseResponse getAllAssignment(Integer page, Integer size){
        var agmList = assignmentRepository.getAllAssignmentNonDeleted(PageRequest.of(page, size));
        return BaseResponse.success(agmList);
    }

    public BaseResponse getAssignmentById(Long asmId){
        try {
            var asmExist = assignmentRepository.findById(asmId)
            .orElseThrow(() -> new RuntimeException("Assignment invalid by id: " + asmId));
//                throw new RuntimeException("Assignment invalid by id: " + asmId);
            var logMessage = asmExist != null
                    ? "Get asm by id success"
                    : "Asm invalid by id " + asmId;
            log.info(logMessage);
            return asmExist == null
                    ? BaseResponse.success("Assignment invalid by id: " + asmId)
                    : BaseResponse.success(asmExist).setMessage("Get asm by id success!");
        } catch (Exception e) {
            log.error("Get asm by id fail: " +e );
            return BaseResponse.error("Get asm by id fail: " + e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseResponse createNewAssignment(AssignmentRequest asmRequest, Authentication authentication){
        try {
            var asmExist = assignmentRepository.findAssignmentByTaskName(asmRequest.getTaskName());
            /*if (asmExist.stream().findAny().isPresent())*/
            if (!asmExist.isEmpty()) {
                throw new RuntimeException("Tên công việc ' " + asmRequest.getTaskName()+ " ' đã tồn tại!");
            }

            var userAssign = userRepository.findByUsername(authentication.getName());
            var asmCreate = new Assignment()
                    .setTaskName(asmRequest.getTaskName())
                    .setTaskDescription(asmRequest.getDescription())
                    .setAssignBy(userAssign);
            asmCreate.setCreation(authentication);

            var asmSave = assignmentRepository.save(asmCreate);

            var asmRes = new AssignmentDto()
                    .setAsmId(asmCreate.getId())
                    .setTaskName(asmCreate.getTaskName())
                    .setTaskDescription(asmCreate.getTaskDescription())
                    .setAssignByUserName(userAssign.getUsername());

            log.info("Create assignment successfully!");
            return BaseResponse.success(asmRes);
        } catch (Exception ex){
            log.error("Create assignment fai: ", ex);
            return BaseResponse.error(ex.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseResponse updateAssignment(Long asmId, AssignmentRequest asmRequest, Authentication authentication){
        try {
             var asmExist = assignmentRepository.findById(asmId)
                    .orElseThrow(() -> new RuntimeException("Assignment invalid by id: " + asmId));

//            if (!assignmentRepository.findAssignmentByTaskName(asmRequest.getTaskName()).isEmpty())
//                throw new RuntimeException("Task name already exist by: " + asmRequest.getTaskName());
            var findTaskNameAsm = assignmentRepository.findAssignmentByTaskName(asmRequest.getTaskName());
            if (!findTaskNameAsm.isEmpty()  && findTaskNameAsm.get(0).getId() !=(asmExist.getId()))
                throw new RuntimeException("Tên công việc ' " + asmRequest.getTaskName()+ " ' đã tồn tại!");
//            if (asmExist.getTaskName().equals(asmRequest.getTaskName()) && !asmExist.getId().equals(asmRequest.getId()))
//                throw new RuntimeException("Task name already exist by: " + asmRequest.getTaskName());
            asmExist.setTaskName(asmRequest.getTaskName())
                    .setTaskDescription(asmRequest.getDescription())
                    .setModification(authentication);

            var asmSave = assignmentRepository.save(asmExist);
            log.info("Update assignment successfully!");
            return BaseResponse.success(asmSave);
        } catch (Exception ex){
            log.error("Update assignment fail!", ex);
            return BaseResponse.error(ex.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseResponse deletedAssignment(Long id, Authentication authentication){
        try {
            var asmExist = assignmentRepository.findById(id)
                    .orElseThrow(() -> new IdNotFoundException("Assignment invalid by id: "+ id));
            asmExist.setIsDeleted(true);
            asmExist.setModification(authentication);
            log.info("Deleted assignment successfully!");

            return BaseResponse.success(asmExist);
        } catch (Exception ex){
            log.error("Deleted assignment fail!", ex);
            return BaseResponse.error(ex.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseResponse deletedAllAssignment(Iterator<Long> asmIds, Authentication authentication){
        try {
            Assert.notNull(asmIds, "id cannot be blank");
            while (asmIds.hasNext()) {
                Long idOfAssignment = asmIds.next();
                var asmIsDeleted = assignmentRepository.findById(idOfAssignment);
                asmIsDeleted.ifPresent(asm -> asm.setIsDeleted(true));
            }
            log.info("Deleted all assignment successfully!");
            return BaseResponse.success(asmIds);
        } catch (Exception ex){
            log.error("Deleted all assignment fail!");
            return BaseResponse.error(ex.getMessage());
        }
    }

    public BaseResponse getListUserInAsm(Long asmId){
        try {
            var lstUser = assignmentRepository.getListUserInAsm(asmId);
            var logMessage = lstUser.size() > 0
                    ? "Get list user in asm success"
                    : "List user in asm by asmId: " + asmId + "is empty";
            log.info(logMessage);
            return lstUser.size() > 0
                    ? BaseResponse.success(lstUser).setMessage("Get list user in asm success")
                    : BaseResponse.success("List user in asm by asmId: " + asmId + "is empty");
        } catch (Exception e){
            log.error("Get list user in asm by asmId" + asmId + "fail: ...", e);
            return BaseResponse.error("Get list user in asm by asmId" + asmId + "fail: ..." + e);
        }
    }

    public BaseResponse getListUserInAsm2(Long asmId){
        try {
            var lstUser = assignmentRepository.getListUserInAsm2(asmId);
            var logMessage = lstUser.size() > 0
                    ? "Get list user in asm success"
                    : "List user in asm by asmId: " + asmId + "is empty";
            log.info(logMessage);
            return lstUser.size() > 0
                    ? BaseResponse.success(lstUser).setMessage("Get list user in asm success")
                    : BaseResponse.success("List user in asm by asmId: " + asmId + "is empty");
        } catch (Exception e){
            log.error("Get list user in asm by asmId" + asmId + "fail: ...", e);
            return BaseResponse.error("Get list user in asm by asmId" + asmId + "fail: ..." + e);
        }
    }

   /* public BaseResponse viewAssignmentDetail(Long id){
        try {
            var asmExist = assignmentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Assignment invalid by id: " + id));
            var asm
        }
    }*/
}

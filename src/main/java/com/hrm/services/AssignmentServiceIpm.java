package com.hrm.services;

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
import java.util.List;

@Service @Slf4j
@RequiredArgsConstructor
public class AssignmentServiceIpm {
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;

    public BaseResponse getAllAssignment(Integer page, Integer size){
        var agmList = assignmentRepository.getAllAssignmentNonDeleted(PageRequest.of(page, size));
        return BaseResponse.success(agmList);
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseResponse createNewAssignment(AssignmentRequest asmRequest, Authentication authentication){
        try {
            var asmExist = assignmentRepository.findAssignmentByTaskName(asmRequest.getTaskName());
            /*if (asmExist.stream().findAny().isPresent())*/
            if (!asmExist.isEmpty()) {
                throw new RuntimeException("Assignment task name is already exist");
            }

            var userAssign = userRepository.findByUsername(authentication.getName());
            var asmCreate = new Assignment()
                    .setTaskName(asmRequest.getTaskName())
                    .setTaskDescription(asmRequest.getDescription())
                    .setAssignBy(userAssign);
            asmCreate.setCreation(authentication);

            var asmSave = assignmentRepository.save(asmCreate);

            var asmRes = new AssignmentDto()
                    .setId(asmCreate.getId())
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
    public BaseResponse updateAssignment(AssignmentRequest asmRequest, Authentication authentication){
        try {
            var asmExist = assignmentRepository.findById(asmRequest.getId())
                    .orElseThrow(() -> new RuntimeException("Assignment invalid by id: " + asmRequest.getId()));

            asmExist.setTaskName(asmRequest.getTaskName())
                    .setTaskDescription(asmExist.getTaskDescription())
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
                    .orElseThrow(() -> new RuntimeException("Assignment invalid by id: "+ id));
            asmExist.setIsDeleted(true);
            asmExist.setModification(authentication);

            var asmSave = assignmentRepository.save(asmExist);
            log.info("Deleted assignment successfully!");

            return BaseResponse.success(asmSave);
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

   /* public BaseResponse viewAssignmentDetail(Long id){
        try {
            var asmExist = assignmentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Assignment invalid by id: " + id));
            var asm
        }
    }*/
}

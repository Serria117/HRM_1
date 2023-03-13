package com.hrm.services;

import com.hrm.payload.BaseResponse;
import com.hrm.payload.TaskUserRequest;
import com.hrm.repositories.AssignmentRepository;
import com.hrm.repositories.TaskUserRepository;
import com.hrm.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service @Slf4j @RequiredArgsConstructor
public class TaskUserServiceImpl {

    private final TaskUserRepository taskUserRepository;
    private final AssignmentRepository assignmentRepository;
    private final UserRepository userRepository;

    public BaseResponse getListTaskUser(Integer page, Integer size){
        Long a = 3L;
        var listUserTask = taskUserRepository.listTaskUser(PageRequest.of(page, size));
        var ab = assignmentRepository.findById(a);
        return BaseResponse.success(listUserTask);
    }

    public BaseResponse getUserListOfAssignment(Long asmId, Integer page, Integer size){
        var listUserOfAsm = taskUserRepository.userListOfAssignment(asmId, PageRequest.of(page, size));
        return BaseResponse.success(listUserOfAsm);
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseResponse createTaskUser(TaskUserRequest tuRequest){
        try {
            var taskUserExist = taskUserRepository.findCurrentUserOfTask(tuRequest.getTask_id(), tuRequest.getAssign_by_id());
            var asmExist = assignmentRepository.findById(tuRequest.getTask_id())
                    .orElseThrow(() -> new RuntimeException("Assignment invalid by id: " + tuRequest.getTask_id()));
            var userExist = userRepository.findById(tuRequest.getAssign_by_id())
                    .orElseThrow(() -> new RuntimeException("User invalid by id: "+ tuRequest.getAssign_by_id()));
            if (taskUserExist.size() > 0)
                throw new RuntimeException("User already exist at work");
            taskUserRepository.createUserOfTask(tuRequest.getTask_id(), tuRequest.getAssign_by_id());
            log.info("Create task user successfully!");
            return BaseResponse.success("Create task user successfully!");
        } catch (Exception ex){
            log.error("Create task user fail!");
            return BaseResponse.error(ex.getMessage());
        }
    }


}

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

    public BaseResponse getListTUByTaskId(Long taskId){
        try {
            var lstTUExist = taskUserRepository.getListTaskUserById(taskId);
            if (lstTUExist ==  null)
                    throw new RuntimeException("TaskUser by taskId invalid");
            var logMessage = lstTUExist.size() > 0
                    ? "Get task user by taskId: " + taskId + " successfully"
                    : "Task by taskId: " + taskId + " is empty!";
            log.info(logMessage);
            return lstTUExist.size() > 0
                    ? BaseResponse.success(lstTUExist)
                    : BaseResponse.success("task user by taskId: " + taskId + " is empty!");
        } catch (Exception e){
            log.info("Get task user by taskId fail: " + e);
            return BaseResponse.error("Get task user by taskId fail: " + e);
        }
    }

    public BaseResponse getTUByTaskId(Long taskId){
        try {
            var tuExist = taskUserRepository.getTaskUserById(taskId)
                    .orElseThrow(() -> new RuntimeException("task user invalid by taskID: " + taskId));
            var logMessage = tuExist != null
                    ? "get task user by taskId success!"
                    : "task user by taskID " + taskId +" is empty";
            log.info(logMessage);
            return tuExist != null
                    ? BaseResponse.success(tuExist).setMessage("get task user by taskId success!")
                    : BaseResponse.success("task user by taskID " + taskId +" is empty");
        } catch (Exception e){
            log.error("get task user by taskId fail: " + e);
            return BaseResponse.error("get task user by taskId fail: " + e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseResponse createTaskUser(TaskUserRequest tuRequest){
        try {
            if (tuRequest.getAssign_by_id() == null)
                throw new RuntimeException("fail");
            var taskUserExist = taskUserRepository.findCurrentUserOfTask(tuRequest.getTask_id(), tuRequest.getAssign_by_id());
            var asmExist = assignmentRepository.findById(tuRequest.getTask_id())
                    .orElseThrow(() -> new RuntimeException("Assignment invalid by id: " + tuRequest.getTask_id()));
            var userExist = userRepository.findById(tuRequest.getAssign_by_id())
                    .orElseThrow(() -> new RuntimeException("User invalid by id: "+ tuRequest.getAssign_by_id()));
            if (taskUserExist.size() > 0)
                throw new RuntimeException("Người này đã ở trong công việc!");
            taskUserRepository.createUserOfTask(tuRequest.getTask_id(), tuRequest.getAssign_by_id());
            log.info("Create task user successfully!");
            return BaseResponse.success("Create task user successfully!");
        } catch (Exception ex){
            log.error("Create task user fail!");
            return BaseResponse.error(ex.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseResponse updateTaskUser(TaskUserRequest tuRequest){
        try {
            var taskUserExist = taskUserRepository.findCurrentUserOfTask(tuRequest.getTask_id(), tuRequest.getAssign_by_id());
            var asmExist = assignmentRepository.findById(tuRequest.getTask_id())
                    .orElseThrow(() -> new RuntimeException("Assignment invalid by id: " + tuRequest.getTask_id()));
            var userExist = userRepository.findById(tuRequest.getAssign_by_id())
                    .orElseThrow(() -> new RuntimeException("User invalid by id: "+ tuRequest.getAssign_by_id()));
            if (taskUserExist.size() > 0)
                throw new RuntimeException("Người này đã ở trong công việc!");
            taskUserRepository.createUserOfTask(tuRequest.getTask_id(), tuRequest.getAssign_by_id());
            log.info("Create task user successfully!");
            return BaseResponse.success("Create task user successfully!");
        } catch (Exception ex){
            log.error("Create task user fail!");
            return BaseResponse.error(ex.getMessage());
        }
    }

//    public BaseResponse addUserOfTask()
}

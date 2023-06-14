package com.hrm.services;

import com.hrm.dto.user.UserDto;
import com.hrm.dtoConverted.DtoConvert;
import com.hrm.entities.AppRole;
import com.hrm.entities.AppUser;
import com.hrm.payload.BaseResponse;
import com.hrm.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service @Slf4j
@RequiredArgsConstructor
public class WorkReportServiceIpm {

    private final UserServiceImpl userService;
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;
    private final LaborContractRepository contractRepository;
    private final DepartmentRepository departmentRepository;
    private final LeaveRepository leaveRepository;
    private final TaskUserRepository taskUserRepository;
    private final AssignmentRepository assignmentRepository;
    private final DtoConvert dtoConvert;

    public BaseResponse eplReport (Integer page, Integer size){
        try {
            var rolesId = Arrays.asList(1L, 4L);
            var roles = roleRepository.findAllById(rolesId);
            var lstEmployee = userRepository.findAllNonDeleted(PageRequest.of(page, size))
                    .stream().filter(s -> !s.getRoles().contains(roles)).map(dtoConvert::convertUserToDto).toList();
            var logMessage = lstEmployee.size() > 0
                    ? "Get list eplReport success!"
                    : "List eplReport is empty";
            log.info(logMessage);
            return lstEmployee.size() > 0
                    ? BaseResponse.success(lstEmployee).setMessage("Get list eplReport success!")
                    : BaseResponse.success("List eplReport is empty");
        } catch (Exception e){
            log.error("Get list eplReport fail: " + e);
            return BaseResponse.error("Get list eplReport fail: " + e);
        }
    }

    public BaseResponse contractReport(Integer page, Integer size){
        try {
            var lstContract = contractRepository.getAllContractNonDeleted(PageRequest.of(page, size)).toList();
            var logMessage = lstContract.size() > 0
                    ? "Get list contractReport success!"
                    : "List contractReport is empty";
            log.info(logMessage);
            return lstContract.size() > 0
                    ? BaseResponse.success(lstContract).setMessage("Get list contractReport success!")
                    : BaseResponse.success("List contractReport is empty");
        } catch (Exception e){
            log.error("Get list contractReport fail: " + e);
            return BaseResponse.error("Get list contractReport fail: " + e);
        }
    }

    public BaseResponse departmentReport(Integer page, Integer size){
        try {
            var lstDepartment = departmentRepository.lstDepartmentViewDetail(PageRequest.of(page, size));
            var logMessage = lstDepartment.size() > 0
                    ? "Get list departmentReport success!"
                    : "List departmentReport is empty";
            log.info(logMessage);
            return lstDepartment.size() > 0
                    ? BaseResponse.success(lstDepartment).setMessage("Get list departmentReport success!")
                    : BaseResponse.success("List departmentReport is empty");
        } catch (Exception e){
            log.error("Get list departmentReport fail: " + e);
            return BaseResponse.error("Get list departmentReport fail: " + e);
        }
    }

    public BaseResponse leaveReport(Integer page, Integer size){
        try {
            var lstLeave = leaveRepository.getAllLeaveNonDeleted(PageRequest.of(page, size)).stream().toList();
            var logMessage = lstLeave.size() > 0
                    ? "Get list leaveReport success!"
                    : "List leaveReport is empty";
            log.info(logMessage);
            return lstLeave.size() > 0
                    ? BaseResponse.success(lstLeave).setMessage("Get list leaveReport success!")
                    : BaseResponse.success("List leaveReport is empty");
        } catch (Exception e){
            log.error("Get list leaveReport fail: " + e);
            return BaseResponse.error("Get list leaveReport fail: " + e);
        }
    }

    public BaseResponse assignmentReport(Integer page, Integer size){
        try {
            var lstAssignment = assignmentRepository.getAllAssignmentNonDeleted(PageRequest.of(page, size)).stream().toList();
            var logMessage = lstAssignment.size() > 0
                    ? "Get list assignmentReport success!"
                    : "List assignmentReport is empty";
            log.info(logMessage);
            return lstAssignment.size() > 0
                    ? BaseResponse.success(lstAssignment).setMessage("Get list assignmentReport success!")
                    : BaseResponse.success("List assignmentReport is empty");
        } catch (Exception e){
            log.error("Get list assignmentReport fail: " + e);
            return BaseResponse.error("Get list assignmentReport fail: " + e);
        }
    }

    public BaseResponse task_userReport(Integer page, Integer size){
        try {
            var lstTaskUser = taskUserRepository.listTaskUser(PageRequest.of(page, size)).stream().toList();
            var logMessage = lstTaskUser.size() > 0
                    ? "Get list task_userReport success!"
                    : "List task_userReport is empty";
            log.info(logMessage);
            return lstTaskUser.size() > 0
                    ? BaseResponse.success(lstTaskUser).setMessage("Get list task_userReport success!")
                    : BaseResponse.success("List task_userReport is empty");
        } catch (Exception e){
            log.error("Get list leaveReport fail: " + e);
            return BaseResponse.error("Get list task_userReport fail: " + e);
        }
    }
}

package com.hrm.services;

import com.hrm.dto.Leave.LeaveDto;
import com.hrm.entities.Leave;
import com.hrm.payload.BaseResponse;
import com.hrm.payload.LeaveRequest;
import com.hrm.repositories.LeaveRepository;
import com.hrm.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service @Slf4j @RequiredArgsConstructor
public class LeaveServiceImpl {
    private final LeaveRepository leaveRepository;
    private final UserRepository userRepository;
    private final DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public BaseResponse getListLeave(Integer page, Integer size){
        try {
            var leave = leaveRepository.getAllLeaveNonDeleted(PageRequest.of(page, size));
            log.info("Get list leave successfully");
            return BaseResponse.success(leave);
        } catch (Exception ex){
            log.error("Get list leave fail!");
            return BaseResponse.error(ex.getMessage());
        }
    }

    public BaseResponse getListLeaveByUser(UUID  userId, Integer page, Integer size){
        try {
            var listLeave = leaveRepository.getListLeaveByUser(userId, PageRequest.of(page, size));
            var logMessage = listLeave.size() > 0
                    ? "Get leaves by userId: " + userId + " successfully"
                    : "Leave by userId: " + userId + " is empty!";
            log.info(logMessage);
            return listLeave.size() > 0
                    ? BaseResponse.success(listLeave)
                    : BaseResponse.success("Leave by userId: " + userId + " is empty!");
        } catch (Exception ex){
            log.error("Get leaves by userId: " + userId + " fail!" + ex.getMessage());
            return BaseResponse.error(ex.getMessage());
        }
    }

    public BaseResponse getLeaveById(Long leaveId){
        try {
            var leave = leaveRepository.findById(leaveId)
                    .orElseThrow(() -> new RuntimeException("Schedule invalid by id: "+ leaveId));
            var epl = userRepository.findById(leave.getRegisterEmployee());
            var leaveDto = new LeaveDto()
                    .setEplName(epl.get().getUsername())
                    .setDateApply(leave.getDateApply().format(formatters));
            var setDurationDto = leave.getDuration() == 0 ? "Cả ngày" : "Nửa ngày";
            leaveDto.setDuration(setDurationDto);
            log.info("Get schedule successfully by id: "+ leaveId);
            return BaseResponse.success(leaveDto);
        } catch (Exception ex){
            log.error("Get schedule by id fail" + ex.getMessage());
            return BaseResponse.error(ex.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseResponse createLeave(LeaveRequest leaveRequest, Authentication authentication){
        try {
            var leaveExist = leaveRepository.findCurrentLeaveByDateApply(leaveRequest.getEplId(), leaveRequest.getDateApply());
            if (leaveExist.isPresent())
                throw new RuntimeException("Scheduled to be off: " + leaveRequest.getDateApply());
            var leave = new Leave()
                    .setRegisterEmployee(leaveRequest.getEplId())
                    .setDuration(leaveRequest.getDuration())
                    .setDateApply(leaveRequest.getDateApply());
            leave.setCreation(authentication);
            var leaveCreate = leaveRepository.save(leave);
            log.info("Register scheduled successfully!");
            return BaseResponse.success(leaveCreate);
        } catch (Exception ex){
            log.error("Register scheduled fail: " + ex.getMessage());
            return BaseResponse.error(ex.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseResponse updateLeave(LeaveRequest leaveRequest, Authentication authentication){
        try {
            var leaveExist = leaveRepository.findById(leaveRequest.getLeaveId())
                    .orElseThrow(() -> new RuntimeException("Schedule invalid"));
            leaveExist.setDuration(leaveRequest.getDuration())
                    .setDateApply(leaveRequest.getDateApply())
                    .setModification(authentication);
            log.info("Update schedule successfully!");
            return BaseResponse.success(leaveExist);
        } catch (Exception ex){
            log.error("Update schedule fail: " + ex.getMessage());
            return BaseResponse.error(ex.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseResponse deletedLeave(Long leaveId, Authentication authentication){
        try {
            var leaveExist = leaveRepository.findById(leaveId)
                    .orElseThrow(() -> new RuntimeException("Schedule invalid"));
            leaveExist.setIsDeleted(true)
                    .setModification(authentication);
            var leaveDeleted = leaveRepository.save(leaveExist);
            log.info("Deleted schedule successfully!");
            return BaseResponse.success(leaveDeleted);
        } catch (Exception ex){
            log.error("Deleted schedule fail: " + ex.getMessage());
            return BaseResponse.error(ex.getMessage());
        }
    }
}

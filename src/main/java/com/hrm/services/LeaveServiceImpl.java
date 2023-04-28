package com.hrm.services;

import com.hrm.dto.Leave.LeaveDto;
import com.hrm.entities.AppUser;
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

import java.time.LocalDate;
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
//            var epl = userRepository.findById(leave.getRegisterEmployee());
//            var leaveDto = new LeaveDto()
//                    .setEplName(epl.get().getUsername())
//                    .setDateApply(leave.getDateApply().format(formatters));
//            var setDurationDto = leave.getDuration() == 0 ? "Cả ngày" : "Nửa ngày";
//            leaveDto.setDuration(setDurationDto);
            var leaveDto = this.convertLeaveToDto(leave);
            log.info("Get schedule successfully by id: "+ leaveId);
            return BaseResponse.success(leaveDto);
        } catch (Exception ex){
            log.error("Get schedule by id fail" + ex.getMessage());
            return BaseResponse.error("Get schedule by id fail" + ex.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseResponse createLeave(LeaveRequest leaveRequest, Authentication authentication){
        try {
            var dateApplyReq = LocalDate.parse(leaveRequest.getDateApply(), formatters);
            var leaveExist = leaveRepository.findCurrentLeaveByDateApply(leaveRequest.getEplId(), leaveRequest.getDateApply());
            if (leaveExist.isPresent())
                throw new RuntimeException("Scheduled to be off: " + leaveRequest.getDateApply());
            var leave = new Leave()
                    .setRegisterEmployee(leaveRequest.getEplId())
                    .setDuration(leaveRequest.getDuration())
                    .setDateApply(dateApplyReq);
            leave.setIsActivated(false);
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
            var dateApplyReq = LocalDate.parse(leaveRequest.getDateApply(), formatters);
            var leaveExist = leaveRepository.findById(leaveRequest.getLeaveId())
                    .orElseThrow(() -> new RuntimeException("Schedule invalid"));
            leaveExist.setDuration(leaveRequest.getDuration())
                    .setDateApply(dateApplyReq)
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
            log.info("Deleted schedule successfully!");
            return BaseResponse.success(leaveExist);
        } catch (Exception ex){
            log.error("Deleted schedule fail: " + ex.getMessage());
            return BaseResponse.error(ex.getMessage());
        }
    }

    public BaseResponse getListLeaveInActiveNoneDeleted(){
        try {
            var lstLeaveInActive = leaveRepository.findAll().stream()
                    .filter(s -> !s.getIsActivated() && !s.getIsDeleted()).map(this::convertLeaveToDto).toList();
            var logMessage = lstLeaveInActive.size() > 0
                    ? "Get list leave inactive success!"
                    : "list leave inactive is empty";
            log.info(logMessage);
            return lstLeaveInActive.size() > 0
                    ? BaseResponse.success(lstLeaveInActive).setMessage("Get list leave inactive success!")
                    : BaseResponse.success("list leave inactive is empty");
        } catch (Exception e){
            log.error("Get list leave inactive fail: " + e);
            return BaseResponse.error("Get list leave inactive fail: " + e);
        }
    }

    public BaseResponse approvedLeave(Long leaveId, Authentication authentication){
        try {
            var leaveExist = leaveRepository.findById(leaveId)
                    .orElseThrow(() -> new RuntimeException("Leave record invalid by leaveId: " + leaveId));
            leaveExist.setIsActivated(true)
                    .setModification(authentication);
            leaveRepository.save(leaveExist);
            var leaveDto = convertLeaveToDto(leaveExist);
            var logMessage = "Approved leave success!";
            log.info(logMessage);
            return BaseResponse.success(leaveDto).setMessage("Approved leave success!");
        } catch (Exception e){
            log.error("Approved leave fail: " + e);
            return BaseResponse.error("Approved leave fail: " + e);
        }
    }

    public BaseResponse listLeaveUserNoneApprove(UUID userId){
        try {
            var userExist = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User invalid by userId: " + userId));
            var lstUserLeaveNoneApprove = leaveRepository.listLeaveUserNoneApprove2(userId);
            var lstUserLeaveNoneApproveDto = lstUserLeaveNoneApprove.stream().map(this::convertLeaveToDto);
            var logMessage = lstUserLeaveNoneApprove.size() > 0
                    ? "Get list leave user none approve success!"
                    : "List leave user none approve is empty!";
            log.info(logMessage);
            return lstUserLeaveNoneApprove.size() > 0
                    ? BaseResponse.success(lstUserLeaveNoneApproveDto).setMessage("Get list leave user none approve success!")
                    : BaseResponse.success("List leave user none approve is empty!");
        } catch (Exception e){
            log.error("Get list user leave none approve fail: " + e);
            return BaseResponse.error("Get list user leave none approve fail: " + e);
        }
    }

    public BaseResponse listLeaveUserApproveNoneDeleted(UUID userId){
        try {
            var userExist = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User invalid by userId: " + userId));
            var lstLeave = leaveRepository.listLeaveUserApproveNoneDeleted(userExist.getId());
            var lstLeaveDto = lstLeave.stream().map(this::convertLeaveToDto);
            var logMessage = lstLeave.size() > 0
                    ? "Get list leave user approve success!"
                    : "List leave user approve is empty!";
            log.info(logMessage);
            return lstLeave.size() > 0
                    ? BaseResponse.success(lstLeaveDto).setMessage("Get list leave user approve success!")
                    : BaseResponse.success("List leave user approve is empty!");
        } catch (Exception e) {
          log.error("Get list leave user approve fail: " + e);
          return BaseResponse.error("Get list leave user approve fail: " + e);
        }
    }

    public LeaveDto convertLeaveToDto(Leave leave){
        var durationDto = leave.getDuration() == 0 ? "Cả ngày" : "Nửa ngày";
//        var isActivatedDto = leave.getIsActivated() ? "Đồng ý" : "";
        return new LeaveDto()
                .setId(leave.getId())
                .setEplName(userRepository.getReferenceById(leave.getRegisterEmployee()).getUsername())
                .setDateApply(leave.getDateApply().format(formatters))
                .setDuration(durationDto)
                .setIsActivated(leave.getIsActivated());
    }
}

package com.hrm.services;

import com.hrm.entities.Attendance;
import com.hrm.payload.AttendanceRequest;
import com.hrm.payload.BaseResponse;
import com.hrm.repositories.AttendanceRepository;
import com.hrm.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;

@Service @Slf4j @RequiredArgsConstructor
public class AttendanceServiceImpl {
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    public BaseResponse getAllAttendance(Integer page, Integer size){
        try {
            var atdList = attendanceRepository.getListAttendance(PageRequest.of(page, size));
            var logMessage = atdList.size() > 0
                    ? "Get list attendance successfully!"
                    : "List attendance is empty!";
            log.info(logMessage);
            return atdList.size() > 0
                    ? BaseResponse.success(atdList).setMessage("Get list attendance successfully!")
                    : BaseResponse.success("List attendance is empty!");
        } catch (Exception ex){
            log.error("Get list attendance fail" + ex.getMessage());
            return BaseResponse.error(ex.getMessage());
        }
    }

    public BaseResponse overviewAttendance(Long atdId){
        try {
            var atd = attendanceRepository.findById(atdId);
            var logMessage = atd.isPresent()
                    ? "Overview attendance successfully!"
                    : "Check-in session does not exist";
            log.info(logMessage);
            return atd.isPresent()
                    ? BaseResponse.success(atd).setMessage("Overview attendance successfully!")
                    : BaseResponse.success("Check-in session does not exist");
        } catch (Exception ex){
            log.info("Overview attendance fail!" + ex.getMessage());
            return BaseResponse.error(ex.getMessage());
        }
    }

    @Transactional(rollbackFor =  Exception.class)
    public BaseResponse checkInAttendance(AttendanceRequest request, Authentication authentication){
        try {
            var userCheckIn = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User invalid by id: " + "' " + request.getUserId() + " '"));
            var checkInAtd = new Attendance()
                    .setUser(userCheckIn)
                    .setCheckInTime(Instant.now())
                    .setAttendantDate(LocalDate.now());
            checkInAtd.setCreation(authentication);
            log.info("Checkin successfully!");
            return BaseResponse.success(checkInAtd).setMessage("Checkin successfully!");
        } catch (Exception ex){
            log.error("Checkin fail! " + ex.getMessage());
            return BaseResponse.error(ex.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public BaseResponse checkOutAttendance(AttendanceRequest request, Authentication authentication){
        try {
            var atdExist = attendanceRepository.findById(request.getAtdId())
                    .orElseThrow(() -> new RuntimeException("Checkout invalid!"));
            atdExist.setCheckOutTime(Instant.now());
            atdExist.setModification(authentication);
            log.info("Checkout successfully!");
            return BaseResponse.success(atdExist).setMessage("Checkout successfully!");
        }catch (Exception ex){
            log.info("Checkout fail! " + ex.getMessage());
            return BaseResponse.error(ex.getMessage());
        }
    }
}

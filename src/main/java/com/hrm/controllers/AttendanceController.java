package com.hrm.controllers;

import com.hrm.payload.AttendanceRequest;
import com.hrm.services.AttendanceServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/attendance")
public class AttendanceController {
    private final AttendanceServiceImpl attendanceService;

    @GetMapping("get-list")
    public ResponseEntity<?> getListAttendance(@RequestParam(required = false, defaultValue = "0") Integer page,
                                               @RequestParam(required = false, defaultValue = "100") Integer size){
        var res = attendanceService.getAllAttendance(page, size);
        return res.getSucceed()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    @GetMapping("overview/{id}")
    public ResponseEntity<?> overviewAttendance(@PathVariable(name = "id") Long atdId){
        var res = attendanceService.overviewAttendance(atdId);
        return res.getSucceed()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    @PostMapping("check-in")
    public ResponseEntity<?> checkIn(AttendanceRequest request, Authentication authentication){
        var res = attendanceService.checkInAttendance(request, authentication);
        return res.getSucceed()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    @PostMapping("check-out")
    public ResponseEntity<?> checkOut(AttendanceRequest request, Authentication authentication){
        var res = attendanceService.checkOutAttendance(request, authentication);
        return res.getSucceed()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }
}

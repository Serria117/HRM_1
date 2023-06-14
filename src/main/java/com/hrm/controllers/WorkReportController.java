package com.hrm.controllers;

import com.hrm.services.WorkReportServiceIpm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/work-report")
public class WorkReportController {

    private final WorkReportServiceIpm workReportServiceIpm;

    @GetMapping("eplReport")
    public ResponseEntity<?> lstEplReport(@RequestParam(required = false, defaultValue = "0") Integer page,
                                          @RequestParam(required = false, defaultValue = "100") Integer size){
        var res = workReportServiceIpm.eplReport(page, size);
        return res.getSucceed()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    @GetMapping("contractReport")
    public ResponseEntity<?> lstContractReport(@RequestParam(required = false, defaultValue = "0") Integer page,
                                          @RequestParam(required = false, defaultValue = "100") Integer size){
        var res = workReportServiceIpm.contractReport(page, size);
        return res.getSucceed()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    @GetMapping("departmentReport")
    public ResponseEntity<?> lstDepartmentReport(@RequestParam(required = false, defaultValue = "0") Integer page,
                                               @RequestParam(required = false, defaultValue = "100") Integer size){
        var res = workReportServiceIpm.departmentReport(page, size);
        return res.getSucceed()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    @GetMapping("leaveReport")
    public ResponseEntity<?> lstLeaveReport(@RequestParam(required = false, defaultValue = "0") Integer page,
                                                 @RequestParam(required = false, defaultValue = "100") Integer size){
        var res = workReportServiceIpm.leaveReport(page, size);
        return res.getSucceed()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    @GetMapping("assignmentReport")
    public ResponseEntity<?> lstAssignmentReport(@RequestParam(required = false, defaultValue = "0") Integer page,
                                               @RequestParam(required = false, defaultValue = "100") Integer size){
        var res = workReportServiceIpm.assignmentReport(page, size);
        return res.getSucceed()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    @GetMapping("task_userReport")
    public ResponseEntity<?> lstTaskUserReport(@RequestParam(required = false, defaultValue = "0") Integer page,
                                            @RequestParam(required = false, defaultValue = "100") Integer size){
        var res = workReportServiceIpm.task_userReport(page, size);
        return res.getSucceed()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }
}

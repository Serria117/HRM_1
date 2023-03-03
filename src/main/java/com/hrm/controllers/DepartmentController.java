package com.hrm.controllers;

import com.hrm.payload.DepartmentRequest;
import com.hrm.services.DepartmentServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/department")
public class DepartmentController {

    private final DepartmentServiceImpl departmentService;

    public DepartmentController(DepartmentServiceImpl departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping("add-new")
    public ResponseEntity<?> createNewDepartment(DepartmentRequest dpmRequest, Authentication authentication){
        var res = departmentService.createDepartment(dpmRequest, authentication);
        return res.getSucceed() ? ResponseEntity.ok(res) : ResponseEntity.badRequest().body(res);
    }

    @PostMapping("change-mng-user")
    public ResponseEntity<?> updateDepartment(@RequestParam Long dpmId, @RequestParam UUID mngUserId, Authentication authentication){
        var res = departmentService.changeUserManagerOfDepartment(dpmId, mngUserId, authentication);
        return res.getSucceed() ? ResponseEntity.ok(res) : ResponseEntity.badRequest().body(res);
    }

    @GetMapping("department-detail/{id}")
    public ResponseEntity<?> departmentViewDetail(@PathVariable(name = "id") Long dpmId){
        var res = departmentService.viewDetailDepartment(dpmId);
        return res.getSucceed() ? ResponseEntity.ok(res) : ResponseEntity.badRequest().body(res);
    }
}

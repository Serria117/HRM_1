package com.hrm.controllers;

import com.hrm.payload.BaseResponse;
import com.hrm.payload.DepartmentRequest;
import com.hrm.services.DepartmentServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/department")
public class DepartmentController {

    private final DepartmentServiceImpl departmentService;

    public DepartmentController(DepartmentServiceImpl departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping("get-all") @Async
    public CompletableFuture<BaseResponse> getAll(@RequestParam(required = false, defaultValue = "0") Integer page,
                                                  @RequestParam(required = false, defaultValue = "100") Integer size)
    {
            return departmentService.getDepartmentList(page, size);
    }

    @PostMapping("add-new") @Async
    public CompletableFuture<BaseResponse> createNewDepartment(DepartmentRequest dpmRequest, Authentication authentication){
        return departmentService.createDepartment(dpmRequest, authentication);
    }

    @PostMapping("update") @Async
    public CompletableFuture<BaseResponse> updateDepartment(DepartmentRequest request, Authentication authentication)
    {
        return departmentService.updateDepartment(request, authentication);
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

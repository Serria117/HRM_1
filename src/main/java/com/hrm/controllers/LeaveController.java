package com.hrm.controllers;

import com.hrm.payload.LeaveRequest;
import com.hrm.services.LeaveServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/leave")
public class LeaveController {
    private final LeaveServiceImpl leaveService;

    @GetMapping("get-list")
    public ResponseEntity<?> getListLeave(@RequestParam(required = false, defaultValue = "0") Integer page,
                                                             @RequestParam(required = false, defaultValue = "100") Integer size) throws ExecutionException, InterruptedException {
        var res = leaveService.getListLeave(page, size);
        return res.getSucceed()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    @GetMapping("get-leaves/{id}")
    public ResponseEntity<?> getLeavesByUser(@PathVariable(name = "id") UUID userId,
                                             @RequestParam(required = false, defaultValue = "0") Integer page,
                                             @RequestParam(required = false, defaultValue = "100") Integer size){
        var res = leaveService.getListLeaveByUser(userId, page, size);
        return res.getSucceed()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getLeaveById(@PathVariable(name = "id") Long leaveId){
        var res = leaveService.getLeaveById(leaveId);
        return res.getSucceed()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    @PostMapping("add-new")
    public ResponseEntity<?> createLeave(@RequestBody LeaveRequest leaveRequest, Authentication authentication) throws ExecutionException, InterruptedException {
        var res = leaveService.createLeave(leaveRequest, authentication);
        return res.getSucceed()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    @PostMapping("update")
    public ResponseEntity<?> updateLeave(@RequestBody LeaveRequest request, Authentication authentication){
        var res = leaveService.updateLeave(request, authentication);
        return res.getSucceed()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    @PostMapping("delete/{id}")
    public ResponseEntity<?> deletedLeave(@PathVariable(name = "id") Long leaveId, Authentication authentication){
        var res = leaveService.deletedLeave(leaveId, authentication);
        return res.getSucceed()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }
}

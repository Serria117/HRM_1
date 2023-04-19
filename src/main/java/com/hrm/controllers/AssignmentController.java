package com.hrm.controllers;

import com.hrm.payload.AssignmentRequest;
import com.hrm.services.AssignmentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController @RequiredArgsConstructor
@RequestMapping("api/assignment")
public class AssignmentController {
    private final AssignmentServiceImpl assignmentService;

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllAssignment(@RequestParam(required = false, defaultValue = "0") Integer page,
                                              @RequestParam(required = false, defaultValue = "100") Integer size){
        var res = assignmentService.getAllAssignment(page, size);
        return res.getSucceed()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    @GetMapping("/getById/{asmId}")
    public ResponseEntity<?> getAsmById(@PathVariable Long asmId){
        var res = assignmentService.getAssignmentById(asmId);
        return res.getSucceed()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    @PostMapping("/add-new")
    public ResponseEntity<?> createAssignment(@RequestBody AssignmentRequest asmRequest, Authentication authentication){
        var objRes = assignmentService.createNewAssignment(asmRequest, authentication);
        return objRes.getSucceed()
                ? ResponseEntity.ok(objRes)
                : ResponseEntity.badRequest().body(objRes);
    }

    @PostMapping("/update/{asmId}")
    public ResponseEntity<?> updateAssignment(@PathVariable Long asmId,
                                              @RequestBody AssignmentRequest request,
                                              Authentication authentication){
        var res = assignmentService.updateAssignment(asmId, request, authentication);
        return res.getSucceed()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    @PostMapping("delete/{asmId}")
    public ResponseEntity<?> deleteAssignment(@PathVariable(name = "asmId") Long asmId, Authentication authentication){
        var res = assignmentService.deletedAssignment(asmId, authentication);
        return  res.getSucceed()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    @GetMapping("getListUserBy/{asmId}")
    public ResponseEntity<?> getListUserInAsm(@PathVariable Long asmId){
        var res = assignmentService.getListUserInAsm(asmId);
        return res.getSucceed()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    /*@PostMapping("/deleted-all")
    public ResponseEntity<?> deletedAllAssignment()*/
}

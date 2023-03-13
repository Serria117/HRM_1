package com.hrm.controllers;

import com.hrm.payload.AssignmentRequest;
import com.hrm.services.AssignmentServiceIpm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequiredArgsConstructor
@RequestMapping("api/assignment")
public class AssignmentController {
    private final AssignmentServiceIpm assignmentService;

    @PostMapping("/add-new")
    public ResponseEntity<?> createAssignment(@RequestBody AssignmentRequest asmRequest, Authentication authentication){
        var objRes = assignmentService.createNewAssignment(asmRequest, authentication);
        return objRes.getSucceed()
                ? ResponseEntity.ok(objRes)
                : ResponseEntity.badRequest().body(objRes);
    }

    /*@PostMapping("/deleted-all")
    public ResponseEntity<?> deletedAllAssignment()*/
}

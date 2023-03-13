package com.hrm.controllers;

import com.hrm.payload.TaskUserRequest;
import com.hrm.services.TaskUserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/task-user")
public class TaskUserController {

    private final TaskUserServiceImpl taskUserService;

    @GetMapping("/get-list")
    public ResponseEntity<?> getListTaskUser(@RequestParam(required = false, defaultValue = "0") Integer page,
                                             @RequestParam(required = false, defaultValue = "100") Integer size){
        var listUserTask = taskUserService.getListTaskUser(page, size);
        return listUserTask.getSucceed()
                ? ResponseEntity.ok(listUserTask)
                : ResponseEntity.badRequest().body(listUserTask);
    }

    @GetMapping("/get-user-assignment")
    public ResponseEntity<?> getUserListOfAssignment(@PathVariable Long asmId,
                                                  @RequestParam(required = false, defaultValue = "0") Integer page,
                                                  @RequestParam(required = false, defaultValue = "100") Integer size){
        var listUserAsm = taskUserService.getUserListOfAssignment(asmId, page, size);
        return listUserAsm.getSucceed()
                ? ResponseEntity.ok(listUserAsm)
                : ResponseEntity.badRequest().body(listUserAsm);
    }

    @PostMapping("/add-new")
    public ResponseEntity<?> createTaskUser(@RequestBody TaskUserRequest tuRequest){
        var res = taskUserService.createTaskUser(tuRequest);
        return res.getSucceed()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }
}

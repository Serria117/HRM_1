package com.hrm.controllers;

import com.hrm.services.RoleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RequestMapping("api/role")
@RequiredArgsConstructor
public class RoleController {
    private final RoleServiceImpl roleService;

    @GetMapping("/get-all")
    public ResponseEntity<?> getListRole(){
        var res = roleService.getAllRole();
        return res.getSucceed()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }
}

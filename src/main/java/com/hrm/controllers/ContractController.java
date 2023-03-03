package com.hrm.controllers;

import com.hrm.configurations.SwaggerConfig;
import com.hrm.payload.LaborContractRequest;
import com.hrm.services.ContractServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/contract")
@SecurityRequirement(name = SwaggerConfig.SECURITY_NAME)
@Slf4j
public class ContractController {

    private final ContractServiceImpl contractService;

    public ContractController(ContractServiceImpl contractService) {
        this.contractService = contractService;
    }

    /*@PostMapping("create")
    public ResponseEntity<?> createNewContract(@RequestBody UUID ){

        *//*return contractService.createNewContract(request.getId(), )*//*
    }*/

    @GetMapping("/{id}")
    public ResponseEntity<?> contractDetail(@PathVariable(name = "id") Long lbContractId){
        var obj = contractService.contractViewDetail(lbContractId);
        return ResponseEntity.ok(obj);
    }

    @PostMapping("/add-new")
    public ResponseEntity<?> createNewContract(@RequestBody LaborContractRequest lbContractRequest, Authentication authentication){
        var res = contractService.createNewContractt(lbContractRequest, authentication);
        return res != null ? ResponseEntity.ok("Create contract success!") : ResponseEntity.badRequest().body(res);
    }

    @PostMapping("/add-new-user-contract")
    public ResponseEntity<?> createNewContractOfUser(@RequestBody LaborContractRequest lbContractRequest, Authentication authentication){
        var res = contractService.createNewContractOfUser(lbContractRequest, authentication);
        return res.getSucceed() ? ResponseEntity.ok(res) : ResponseEntity.badRequest().body(res);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateContract(@RequestParam Long lbContractId, @RequestParam Double baseSalary, Authentication authentication){
        var res = contractService.updateContractOfUser(lbContractId, baseSalary, authentication);
        return res ? ResponseEntity.ok("Update contract success!") :ResponseEntity.badRequest().body(false);
    }
}

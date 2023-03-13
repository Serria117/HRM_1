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
    @GetMapping("/get-list")

    public ResponseEntity<?> getListContract(@RequestParam(required = false, defaultValue = "0") Integer page,
                                             @RequestParam(required = false, defaultValue = "100") Integer size){
        var objRes = contractService.getListLbContract(page, size);
        return objRes.getSucceed() ?
                ResponseEntity.ok(objRes)
                : ResponseEntity.badRequest().body(objRes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> contractDetail(@PathVariable(name = "id") Long lbContractId){
        var obj = contractService.contractViewDetail(lbContractId);
        return ResponseEntity.ok(obj);
    }

    @PostMapping("/add-new")
    public ResponseEntity<?> createNewContract(@RequestBody LaborContractRequest lbContractRequest, Authentication authentication){
        var res = contractService.createNewContract(lbContractRequest, authentication);
        return res.getSucceed() ?
                ResponseEntity.ok("Create contract success!")
                : ResponseEntity.badRequest().body(res);
    }

    @PostMapping("/add-new-user-contract")
    public ResponseEntity<?> createNewContractOfUser(@RequestBody LaborContractRequest lbContractRequest, Authentication authentication){
        var res = contractService.createNewContractOfUser(lbContractRequest, authentication);
        return res.getSucceed() ?
                ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateContract(@RequestParam Long lbContractId, @RequestParam Double baseSalary, Authentication authentication){
        var res = contractService.updateContract(lbContractId, baseSalary, authentication);
        return res.getSucceed() ?
                ResponseEntity.ok(res)
                :ResponseEntity.badRequest().body(res);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteContract(@PathVariable(name = "id") Long lbContractId, Authentication authentication){
        var res = contractService.deleteContract(lbContractId, authentication);
        return  res.getSucceed() ?
                ResponseEntity.ok("Delete contract successfully!")
                : ResponseEntity.badRequest().body(res);
    }

    @GetMapping("/test/{id}")
    public ResponseEntity<?> testCurrentContract(@PathVariable(name = "id") UUID userId){
        var res = contractService.testCurrentContract(userId);
        return res.getSucceed() ? ResponseEntity.ok(res) : ResponseEntity.badRequest().body(res);
    }
}

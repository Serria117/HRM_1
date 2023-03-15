package com.hrm.controllers;

import com.hrm.configurations.SwaggerConfig;
import com.hrm.payload.BaseResponse;
import com.hrm.payload.LaborContractRequest;
import com.hrm.services.ContractServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/contract")
@SecurityRequirement(name = SwaggerConfig.SECURITY_NAME)
@Slf4j
public class ContractController {

    private final ContractServiceImpl contractService;

    public ContractController(ContractServiceImpl contractService) {
        this.contractService = contractService;
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getListContract(@RequestParam(required = false, defaultValue = "0") Integer page,
                                             @RequestParam(required = false, defaultValue = "100") Integer size){
        var res = contractService.getAllContract(page, size);
        return res.getSucceed()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> contractDetail(@PathVariable(name = "id") Long lbContractId){
        var res = contractService.contractViewDetail(lbContractId);
        return res.getSucceed()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }

    @PostMapping("/add-new")
    public BaseResponse createNewContract(@RequestBody LaborContractRequest lbContractRequest, Authentication authentication){
        return contractService.createNewContract(lbContractRequest, authentication);
    }

    @PostMapping("/update")
    public BaseResponse updateContract(@RequestBody LaborContractRequest request,
                                            Authentication authentication){
        return contractService.updateContract(request, authentication);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteContract(@PathVariable(name = "id") Long lbContractId, Authentication authentication){
        var res = contractService.deleteContract(lbContractId, authentication);
        return res.getSucceed()
                ? ResponseEntity.ok(res)
                : ResponseEntity.badRequest().body(res);
    }
}

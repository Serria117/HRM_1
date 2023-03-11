package com.hrm.controllers;

import com.hrm.configurations.SwaggerConfig;
import com.hrm.payload.BaseResponse;
import com.hrm.payload.LaborContractRequest;
import com.hrm.services.ContractServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("/{id}")
    public BaseResponse contractDetail(@PathVariable(name = "id") Long lbContractId){
        return contractService.contractViewDetail(lbContractId);
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
}

package com.hrm.controllers;

import com.baseapp.services.contract.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.util.annotation.Nullable;

import java.time.LocalDate;
import java.util.UUID;

@RestController @RequiredArgsConstructor @RequestMapping("/contract")
public class ContractController
{
    private ContractService contractService;

    @PostMapping("add")
    public void addNewContract(UUID userId,
                               Long contractTypeId,
                               LocalDate startDate,
                               @Nullable LocalDate endDate,
                               Authentication authentication)
    {

    }
}

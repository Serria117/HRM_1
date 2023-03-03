package com.hrm.payload;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Data @Accessors(chain = true)
public class LaborContractRequest implements Serializable {
    private Long id;
    private String contractNumber;
    private Double basicSalary;
    private UUID userId;
    private Long contractTypeId;
    private LocalDate startDate;
    private LocalDate endDate;
}

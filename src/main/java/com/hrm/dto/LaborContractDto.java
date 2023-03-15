package com.hrm.dto;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

@Data @Accessors(chain = true)
@AllArgsConstructor @NoArgsConstructor
public class LaborContractDto{
    private Long id;
    private String fullName;
    private String contractNumber;
    private String contractTypeName;
    private String email;
    private String phone;
    private String startDate;
    private String endDate;
}

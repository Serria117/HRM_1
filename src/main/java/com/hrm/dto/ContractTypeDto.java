package com.hrm.dto;

import lombok.*;

@Data @Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class ContractTypeDto {
    private Long id;
    private String contractNumber;
    private String createByUser;
    private Double basicSalary;
    private Boolean isActivated;
    private String contractTypeName;
    private String contractOfUser;
}

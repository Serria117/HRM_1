package com.hrm.dto;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

@Data @Getter @Setter @Builder @Accessors(chain = true)
@AllArgsConstructor @NoArgsConstructor
public class LaborContractDto implements Serializable {
    private Long id;
    private String contractOfUser;
    private String contractNumber;
    private String contractTypeName;
    private String emailOfUser;
    private String phoneOfUser;
    private LocalDate startDate;
    private LocalDate endDate;
}

package com.hrm.dto.laborContract;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

@Data @Getter @Setter @Builder @Accessors(chain = true)
@AllArgsConstructor @NoArgsConstructor
public class LaborContractDto implements Serializable {
    private Long lbId;
    private String lbUserName;
    private String lbNumber;
    private String lbTypeName;
    private String lbUserEmail;
    private String lbUserPhone;
    private LocalDate startDate;
    private LocalDate endDate;
}

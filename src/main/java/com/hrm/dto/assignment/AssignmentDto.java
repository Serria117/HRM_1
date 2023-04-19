package com.hrm.dto.assignment;

import com.hrm.dto.user.UserDto;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class AssignmentDto {
    private Long asmId;
    private String taskName;
    private String taskDescription;
    private String assignByUserName;
    private Integer numberOfUserAssign;
    private Boolean isActivated;
    private String userInAssign;
    private List<UserDto> lstUserInAsm;
    private String hrefInfoAsm;
}

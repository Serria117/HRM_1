package com.hrm.dto.assignment;

import com.hrm.dto.UserDto;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class AssignmentDto {
    private Long id;
    private String taskName;
    private String taskDescription;
    private String assignByUserName;
    private Integer numberOfUserAssign;
    private List<UserDto> userInAssign;
}

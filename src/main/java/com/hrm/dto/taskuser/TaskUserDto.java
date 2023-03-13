package com.hrm.dto.taskuser;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.UUID;

@Data
@Getter
@Setter
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class TaskUserDto implements Serializable {
    private Long taskId;
    private UUID assignById;
    private String taskName;
    private String assignByName;
    private String taskDescription;
}

package com.hrm.dto.taskuser.taskUserProjection;

import java.util.UUID;

public interface TaskUserViewDto {
    Long taskId();
    UUID assignById();
    String taskName();
    String assignByName();
    String taskDescription();
}

package com.hrm.payload;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data @Builder
@NoArgsConstructor @Accessors(chain = true)
@AllArgsConstructor @Getter @Setter
public class TaskUserRequest {
    private Long task_id;
    private UUID assign_by_id;
}

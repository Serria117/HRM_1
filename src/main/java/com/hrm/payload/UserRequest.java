package com.hrm.payload;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class UserRequest implements Serializable {
    private UUID id;

}

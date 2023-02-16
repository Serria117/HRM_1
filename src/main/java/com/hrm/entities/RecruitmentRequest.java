package com.hrm.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.UUID;

@Getter @Setter @Accessors(chain = true)
@Entity
public class RecruitmentRequest extends BaseEntity
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private Long departmentId;
    private UUID requestUser;
    private String description;
    private Long numberEmployeeRequest;
    private Long numberOfEmployeeRecruited;
}

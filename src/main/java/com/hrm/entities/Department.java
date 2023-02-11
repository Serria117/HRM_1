package com.hrm.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity @Getter @Setter @Accessors(chain = true)
public class Department extends BaseEntity
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Size(max = 100)
    private String departmentName;
    @Size(max = 10)
    private String departmentCode;

    @OneToOne @JoinColumn(name = "mng_user_id")
    private AppUser managementUser;
}

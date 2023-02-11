package com.hrm.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity @Getter @Setter @Accessors(chain = true)
@Table(name = "job_title")
public class JobTitle extends BaseEntity
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Size(max = 200)
    private String title;
    @Size(max = 10)
    private String code;
}

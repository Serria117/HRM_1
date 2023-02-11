package com.hrm.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity @Getter @Setter @Accessors(chain = true) @Table(name = "labor_contract")
public class LaborContract extends BaseEntity
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Size(max = 20)
    private String contractNumber;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer contractTypeId;
}

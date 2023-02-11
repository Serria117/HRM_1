package com.hrm.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Getter @Setter @Accessors(chain = true)
@Entity @Table(name = "benefit")
public class Benefit extends BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String benefitName;
    private Double value;
    private Long contractId;
}

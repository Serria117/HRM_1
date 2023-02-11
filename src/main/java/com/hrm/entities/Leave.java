package com.hrm.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Getter @Setter @Accessors(chain = true)
@Entity @Table(name = "leave_record")
public class Leave extends BaseEntity
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false) private
    Long id;
    LocalDate dateApply;
    Integer duration; //0=full day, 1 = 0.5 day
    UUID registerEmployee;
}

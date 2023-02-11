package com.hrm.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

@Getter @Setter @Entity
@Table(name = "attendance") @Accessors(chain = true)
public class Attendance extends BaseEntity implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate attendantDate;
    private Instant checkInTime;
    private Instant checkOutTime;
    @ManyToOne @JoinColumn(name = "user_id")
    private AppUser user;
}

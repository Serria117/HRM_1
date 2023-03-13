package com.hrm.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter @Setter @Accessors(chain = true)
@Entity @Table(name = "assignment")
public class Assignment extends BaseEntity
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String taskName;
    private String taskDescription;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "task_user",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "assign_by_id"))
    private Set<AppUser> assignTo = new LinkedHashSet<>();

    @ManyToOne @JoinColumn(name = "assign_by_id")
    private AppUser assignBy;
}

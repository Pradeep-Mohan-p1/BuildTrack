package com.example.buildtrack.assignment.entity;

import com.example.buildtrack.employee.entity.Employee;
import com.example.buildtrack.project.entity.Project;
import com.example.buildtrack.site.entity.ConstructionSite;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "employee_assignments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeProjectAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", nullable = false)
    private ConstructionSite site;

    private LocalDate assignedDate;

    private LocalDate endDate;

    @Builder.Default
    private Boolean active = true;
}

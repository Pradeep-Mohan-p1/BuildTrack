package com.example.buildtrack.employee.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phone;

    private String designation;

    private String department;

    @Column(nullable = false)
    private Double dailyWage;

    private Double monthlySalary;

    private LocalDate joiningDate;

    @Builder.Default
    private Boolean active = true;
}


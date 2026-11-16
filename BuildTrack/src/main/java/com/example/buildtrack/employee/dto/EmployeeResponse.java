package com.example.buildtrack.employee.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {

    private Long id;

    private String name;

    private String email;

    private String phone;

    private String designation;

    private String department;

    private Double dailyWage;

    private Double monthlySalary;

    private LocalDate joiningDate;

    private Boolean active;
}

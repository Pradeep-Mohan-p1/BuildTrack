package com.example.buildtrack.employee.dto;

import lombok.*;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {
     private String name;

    private String email;

    private String phone;

    private String designation;

    private String department;

    private Double dailyWage;

    private Double monthlySalary;

    private LocalDate joiningDate;
}

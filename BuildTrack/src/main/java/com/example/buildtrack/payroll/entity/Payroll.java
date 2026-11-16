
package com.example.buildtrack.payroll.entity;

import com.example.buildtrack.employee.entity.Employee;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "payroll",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_employee_payroll_period",
        columnNames = {"employee_id", "payroll_month", "payroll_year"}
    )
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(name = "payroll_month", nullable = false)
    private Integer month;

    @Column(name = "payroll_year", nullable = false)
    private Integer year;

    private Integer presentDays;

    private Integer absentDays;

    private Integer halfDays;

    private BigDecimal overtimeHours;

    @Column(precision = 15, scale = 2)
    private BigDecimal basicSalary;

    @Column(precision = 15, scale = 2)
    private BigDecimal overtimePay;

    @Column(precision = 15, scale = 2)
    private BigDecimal deductions;

    @Column(precision = 15, scale = 2)
    private BigDecimal grossSalary;

    @Column(precision = 15, scale = 2)
    private BigDecimal netSalary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private PayrollStatus status = PayrollStatus.GENERATED;

    private LocalDateTime generatedAt;
}

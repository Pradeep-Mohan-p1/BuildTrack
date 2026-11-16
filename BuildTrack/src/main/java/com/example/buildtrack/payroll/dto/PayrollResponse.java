package com.example.buildtrack.payroll.dto;

import com.example.buildtrack.payroll.entity.PayrollStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class PayrollResponse {

    private Long id;

    private Long employeeId;
    private String employeeName;

    private Integer month;
    private Integer year;

    private Integer presentDays;
    private Integer absentDays;
    private Integer halfDays;

    private BigDecimal overtimeHours;

    private BigDecimal basicSalary;
    private BigDecimal overtimePay;

    private BigDecimal deductions;
    private BigDecimal grossSalary;
    private BigDecimal netSalary;

    private PayrollStatus status;
}

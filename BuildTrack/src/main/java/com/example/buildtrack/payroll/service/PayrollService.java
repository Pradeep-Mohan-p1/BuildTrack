package com.example.buildtrack.payroll.service;

import com.example.buildtrack.employee.entity.Employee;
import com.example.buildtrack.employee.repository.EmployeeRepository;

import com.example.buildtrack.attendance.entity.Attendance;
import com.example.buildtrack.attendance.repository.AttendanceRepository;

import com.example.buildtrack.payroll.dto.PayrollRequest;
import com.example.buildtrack.payroll.dto.PayrollResponse;
import com.example.buildtrack.payroll.dto.PaymentRequest;

import com.example.buildtrack.payroll.entity.Payroll;
import com.example.buildtrack.payroll.entity.PayrollStatus;
import com.example.buildtrack.payroll.entity.Payment;

import com.example.buildtrack.payroll.repository.PayrollRepository;
import com.example.buildtrack.payroll.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.time.LocalDate;
import java.time.YearMonth;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PayrollService {

    private final PayrollRepository payrollRepository;
    private final PaymentRepository paymentRepository;

    private final EmployeeRepository employeeRepository;
    private final AttendanceRepository attendanceRepository;


    // ------------------------------------------------
    // GENERATE PAYROLL
    // ------------------------------------------------

    public PayrollResponse generatePayroll(PayrollRequest request) {

        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));

        if (!Boolean.TRUE.equals(employee.getActive())) {
            throw new RuntimeException("Inactive employee cannot generate payroll");
        }

        int month = request.getMonth();
        int year = request.getYear();

        if (YearMonth.of(year, month).isAfter(YearMonth.now())) {
            throw new RuntimeException("Cannot generate payroll for a future month");
        }

        boolean alreadyExists =
                payrollRepository.findByEmployeeIdAndMonthAndYear(
                        employee.getId(), month, year
                ).isPresent();

        if (alreadyExists) {
            throw new RuntimeException("Payroll already generated for this employee and month");
        }

        LocalDate startDate = YearMonth.of(year, month).atDay(1);
        LocalDate endDate = YearMonth.of(year, month).atEndOfMonth();

        List<Attendance> attendanceList =
                attendanceRepository.findByEmployeeIdAndAttendanceDateBetween(
                        employee.getId(), startDate, endDate
                );

        int presentDays = 0;
        int absentDays = 0;
        int halfDays = 0;

        BigDecimal overtimeHours = BigDecimal.ZERO;

        for (Attendance attendance : attendanceList) {

            switch (attendance.getStatus()) {

                case PRESENT:
                    presentDays++;
                    break;

                case ABSENT:
                    absentDays++;
                    break;

                case HALF_DAY:
                    halfDays++;
                    break;

                default:
                    break;
            }

            if (attendance.getOvertimeHours() != null) {
                overtimeHours = overtimeHours.add(
                        BigDecimal.valueOf(attendance.getOvertimeHours())
                );
            }
        }

        // Calculate basic salary

        BigDecimal monthlySalary = employee.getMonthlySalary() == null
                ? null
                : BigDecimal.valueOf(employee.getMonthlySalary());
        BigDecimal dailyWage = employee.getDailyWage() == null
                ? null
                : BigDecimal.valueOf(employee.getDailyWage());

        BigDecimal  basicSalary;

        if (monthlySalary != null && monthlySalary.compareTo(BigDecimal.ZERO) > 0) {

            basicSalary = monthlySalary;

        } else if (dailyWage != null && dailyWage.compareTo(BigDecimal.ZERO) > 0) {

            BigDecimal payableDays = BigDecimal.valueOf(presentDays)
                    .add(BigDecimal.valueOf(halfDays)
                            .multiply(new BigDecimal("0.5")));

            basicSalary = dailyWage.multiply(payableDays);

        } else {
            throw new RuntimeException("Employee salary information is missing");
        }

        // Calculate hourly wage

        BigDecimal hourlyWage;

        if (monthlySalary != null && monthlySalary.compareTo(BigDecimal.ZERO) > 0) {

            hourlyWage = monthlySalary.divide(
                    new BigDecimal("208"),
                    2,
                    RoundingMode.HALF_UP
            );

        } else {

            hourlyWage = dailyWage.divide(
                    new BigDecimal("8"),
                    2,
                    RoundingMode.HALF_UP
            );
        }

        // Overtime pay = overtime hours x hourly wage x 1.5

        BigDecimal overtimePay = overtimeHours
                .multiply(hourlyWage)
                .multiply(new BigDecimal("1.5"))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal grossSalary = basicSalary.add(overtimePay);

        BigDecimal deductions = request.getDeductions();

        if (deductions.compareTo(grossSalary) > 0) {
            throw new RuntimeException("Deductions cannot exceed gross salary");
        }

        BigDecimal netSalary = grossSalary.subtract(deductions);

        // Save payroll

        Payroll payroll = Payroll.builder()
                .employee(employee)
                .month(month)
                .year(year)
                .presentDays(presentDays)
                .absentDays(absentDays)
                .halfDays(halfDays)
                .overtimeHours(overtimeHours)
                .basicSalary(basicSalary)
                .overtimePay(overtimePay)
                .deductions(deductions)
                .grossSalary(grossSalary)
                .netSalary(netSalary)
                .status(PayrollStatus.GENERATED)
                .build();

        Payroll savedPayroll = payrollRepository.save(payroll);

        return mapToResponse(savedPayroll);
    }


    // ------------------------------------------------
    // GET ALL PAYROLLS
    // ------------------------------------------------

    @Transactional(readOnly = true)
    public List<PayrollResponse> getAllPayrolls() {

        return payrollRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // ------------------------------------------------
    // GET PAYROLL BY ID
    // ------------------------------------------------

    @Transactional(readOnly = true)
    public PayrollResponse getPayrollById(Long id) {

        Payroll payroll = getPayrollEntity(id);

        return mapToResponse(payroll);
    }


    // ------------------------------------------------
    // GET PAYROLL BY EMPLOYEE
    // ------------------------------------------------

    @Transactional(readOnly = true)
    public List<PayrollResponse> getPayrollsByEmployee(Long employeeId) {

        return payrollRepository.findByEmployeeId(employeeId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // ------------------------------------------------
    // GET PAYROLL BY MONTH AND YEAR
    // ------------------------------------------------

    @Transactional(readOnly = true)
    public List<PayrollResponse> getPayrollsByPeriod(Integer month, Integer year) {

        if (month == null || month < 1 || month > 12) {
            throw new RuntimeException("Month must be between 1 and 12");
        }

        if (year == null || year < 2000) {
            throw new RuntimeException("Invalid year");
        }

        return payrollRepository.findByMonthAndYear(month, year)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // ------------------------------------------------
    // APPROVE PAYROLL
    // ------------------------------------------------

    public PayrollResponse approvePayroll(Long id) {

        Payroll payroll = getPayrollEntity(id);

        if (payroll.getStatus() != PayrollStatus.GENERATED) {
            throw new RuntimeException("Only generated payroll can be approved");
        }

        payroll.setStatus(PayrollStatus.APPROVED);

        return mapToResponse(payrollRepository.save(payroll));
    }


    // ------------------------------------------------
    // REJECT PAYROLL
    // ------------------------------------------------

    public PayrollResponse rejectPayroll(Long id) {

        Payroll payroll = getPayrollEntity(id);

        if (payroll.getStatus() != PayrollStatus.GENERATED) {
            throw new RuntimeException("Only generated payroll can be rejected");
        }

        payroll.setStatus(PayrollStatus.REJECTED);

        return mapToResponse(payrollRepository.save(payroll));
    }


    // ------------------------------------------------
    // MARK PAYROLL AS PAID
    // ------------------------------------------------

    public PayrollResponse markPayrollPaid(
            Long payrollId,
            PaymentRequest request) {

        Payroll payroll = getPayrollEntity(payrollId);

        if (payroll.getStatus() != PayrollStatus.APPROVED) {
            throw new RuntimeException("Only approved payroll can be paid");
        }

        if (request.getAmount().compareTo(payroll.getNetSalary()) != 0) {
            throw new RuntimeException("Payment amount must equal net salary");
        }

        if (request.getPaymentDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("Payment date cannot be in the future");
        }

        if (paymentRepository.existsByPaymentReference(
                request.getPaymentReference())) {
            throw new RuntimeException("Payment reference already exists");
        }

        Payment payment = Payment.builder()
                .payroll(payroll)
                .amount(request.getAmount())
                .paymentDate(request.getPaymentDate())
                .paymentReference(request.getPaymentReference())
                .paymentMode(request.getPaymentMode())
                .build();

        paymentRepository.save(payment);

        payroll.setStatus(PayrollStatus.PAID);

        Payroll savedPayroll = payrollRepository.save(payroll);

        return mapToResponse(savedPayroll);
    }


    // ------------------------------------------------
    // HELPER: GET PAYROLL ENTITY
    // ------------------------------------------------

    private Payroll getPayrollEntity(Long id) {

        return payrollRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Payroll not found with ID: " + id));
    }


    // ------------------------------------------------
    // HELPER: CONVERT ENTITY TO RESPONSE DTO
    // ------------------------------------------------

    private PayrollResponse mapToResponse(Payroll payroll) {

        return PayrollResponse.builder()
                .id(payroll.getId())
                .employeeId(payroll.getEmployee().getId())
                .employeeName(payroll.getEmployee().getName())
                .month(payroll.getMonth())
                .year(payroll.getYear())
                .presentDays(payroll.getPresentDays())
                .absentDays(payroll.getAbsentDays())
                .halfDays(payroll.getHalfDays())
                .overtimeHours(payroll.getOvertimeHours())
                .basicSalary(payroll.getBasicSalary())
                .overtimePay(payroll.getOvertimePay())
                .deductions(payroll.getDeductions())
                .grossSalary(payroll.getGrossSalary())
                .netSalary(payroll.getNetSalary())
                .status(payroll.getStatus())
                .build();
    }
}

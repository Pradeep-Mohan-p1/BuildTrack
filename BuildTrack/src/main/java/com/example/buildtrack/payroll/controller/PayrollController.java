package com.example.buildtrack.payroll.controller;

import com.example.buildtrack.payroll.dto.PayrollRequest;
import com.example.buildtrack.payroll.dto.PayrollResponse;
import com.example.buildtrack.payroll.dto.PaymentRequest;
import com.example.buildtrack.payroll.service.PayrollService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payroll")
@RequiredArgsConstructor
public class PayrollController {

    private final PayrollService payrollService;

    @PostMapping("/generate")
    @ResponseStatus(HttpStatus.CREATED)
    public PayrollResponse generatePayroll(
            @Valid @RequestBody PayrollRequest request) {
        return payrollService.generatePayroll(request);
    }

    @GetMapping
    public List<PayrollResponse> getAllPayrolls() {
        return payrollService.getAllPayrolls();
    }

    @GetMapping("/{id}")
    public PayrollResponse getPayrollById(@PathVariable Long id) {
        return payrollService.getPayrollById(id);
    }

    @GetMapping("/employee/{employeeId}")
    public List<PayrollResponse> getPayrollsByEmployee(
            @PathVariable Long employeeId) {
        return payrollService.getPayrollsByEmployee(employeeId);
    }

    @GetMapping("/period")
    public List<PayrollResponse> getPayrollsByPeriod(
            @RequestParam Integer month,
            @RequestParam Integer year) {
        return payrollService.getPayrollsByPeriod(month, year);
    }

    @PatchMapping("/{id}/approve")
    public PayrollResponse approvePayroll(@PathVariable Long id) {
        return payrollService.approvePayroll(id);
    }

    @PatchMapping("/{id}/reject")
    public PayrollResponse rejectPayroll(@PathVariable Long id) {
        return payrollService.rejectPayroll(id);
    }

    @PostMapping("/{id}/payment")
    public PayrollResponse markPayrollPaid(
            @PathVariable Long id,
            @Valid @RequestBody PaymentRequest request) {
        return payrollService.markPayrollPaid(id, request);
    }
}
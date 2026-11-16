
package com.example.buildtrack.expense.controller;

import com.example.buildtrack.expense.dto.ExpenseRequest;
import com.example.buildtrack.expense.dto.ExpenseResponse;
import com.example.buildtrack.expense.entity.*;
import com.example.buildtrack.expense.service.ExpenseService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseResponse> addExpense(
            @RequestBody ExpenseRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(expenseService.addExpense(request));
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses() {
        return ResponseEntity.ok(expenseService.getAllExpenses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getExpenseById(
            @PathVariable Long id) {

        return ResponseEntity.ok(expenseService.getExpenseById(id));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<ExpenseResponse>> getProjectExpenses(
            @PathVariable Long projectId) {

        return ResponseEntity.ok(
                expenseService.getProjectExpenses(projectId));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<ExpenseResponse>> getByCategory(
            @PathVariable ExpenseCategory category) {

        return ResponseEntity.ok(
                expenseService.getExpensesByCategory(category));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ExpenseResponse>> getByStatus(
            @PathVariable ExpenseStatus status) {

        return ResponseEntity.ok(
                expenseService.getExpensesByStatus(status));
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<ExpenseResponse> approveExpense(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                expenseService.approveExpense(id));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<ExpenseResponse> rejectExpense(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                expenseService.rejectExpense(id));
    }

    @GetMapping("/monthly-approved-total")
    public ResponseEntity<java.math.BigDecimal> getMonthlyTotal(
            @RequestParam int month,
            @RequestParam int year) {

        return ResponseEntity.ok(
                expenseService.getMonthlyApprovedExpenses(month, year));
    }
}

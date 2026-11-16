
package com.example.buildtrack.expense.service;

import com.example.buildtrack.expense.dto.ExpenseRequest;
import com.example.buildtrack.expense.dto.ExpenseResponse;
import com.example.buildtrack.expense.entity.*;
import com.example.buildtrack.expense.repository.ExpenseRepository;
import com.example.buildtrack.project.entity.Project;
import com.example.buildtrack.project.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ProjectRepository projectRepository;

    private ExpenseResponse mapToResponse(Expense expense) {

        return ExpenseResponse.builder()
                .id(expense.getId())
                .projectId(expense.getProject().getId())
                .projectName(expense.getProject().getProjectName())
                .description(expense.getDescription())
                .category(expense.getCategory())
                .amount(expense.getAmount())
                .expenseDate(expense.getExpenseDate())
                .vendorName(expense.getVendorName())
                .invoiceNumber(expense.getInvoiceNumber())
                .status(expense.getStatus())
                .build();
    }

    // Add expense
    public ExpenseResponse addExpense(ExpenseRequest request) {

        validateRequest(request);

        Project project = projectRepository.findById(
                request.getProjectId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Project not found"));

        if (project.getStatus() == com.example.buildtrack.project.entity.ProjectStatus.CANCELLED
                || project.getStatus() == com.example.buildtrack.project.entity.ProjectStatus.COMPLETED) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cannot add expenses to a completed or cancelled project");
        }

        Expense expense = Expense.builder()
                .project(project)
                .description(request.getDescription())
                .category(request.getCategory())
                .amount(request.getAmount())
                .expenseDate(request.getExpenseDate() == null
                        ? LocalDate.now()
                        : request.getExpenseDate())
                .vendorName(request.getVendorName())
                .invoiceNumber(request.getInvoiceNumber())
                .status(ExpenseStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        return mapToResponse(expenseRepository.save(expense));
    }

    // Get all expenses
    @Transactional(readOnly = true)
    public List<ExpenseResponse> getAllExpenses() {

        return expenseRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Get expense by ID
    @Transactional(readOnly = true)
    public ExpenseResponse getExpenseById(Long id) {
        return mapToResponse(findExpense(id));
    }

    // Get project expenses
    @Transactional(readOnly = true)
    public List<ExpenseResponse> getProjectExpenses(Long projectId) {

        return expenseRepository.findByProjectId(projectId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Filter by category
    @Transactional(readOnly = true)
    public List<ExpenseResponse> getExpensesByCategory(
            ExpenseCategory category) {

        return expenseRepository.findByCategory(category)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Filter by status
    @Transactional(readOnly = true)
    public List<ExpenseResponse> getExpensesByStatus(
            ExpenseStatus status) {

        return expenseRepository.findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Approve expense
    public ExpenseResponse approveExpense(Long id) {

        Expense expense = findExpense(id);

        if (expense.getStatus() != ExpenseStatus.PENDING) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only pending expenses can be approved");
        }

        expense.setStatus(ExpenseStatus.APPROVED);

        return mapToResponse(expenseRepository.save(expense));
    }

    // Reject expense
    public ExpenseResponse rejectExpense(Long id) {

        Expense expense = findExpense(id);

        if (expense.getStatus() != ExpenseStatus.PENDING) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only pending expenses can be rejected");
        }

        expense.setStatus(ExpenseStatus.REJECTED);

        return mapToResponse(expenseRepository.save(expense));
    }

    // Calculate approved expense total for a month
    @Transactional(readOnly = true)
    public BigDecimal getMonthlyApprovedExpenses(
            int month, int year) {

        YearMonth period = YearMonth.of(year, month);

        return expenseRepository.findByExpenseDateBetween(
                        period.atDay(1), period.atEndOfMonth())
                .stream()
                .filter(e -> e.getStatus() == ExpenseStatus.APPROVED)
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void validateRequest(ExpenseRequest request) {

        if (request.getProjectId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Project ID is required");
        }

        if (request.getDescription() == null
                || request.getDescription().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Description is required");
        }

        if (request.getCategory() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Category is required");
        }

        if (request.getAmount() == null
                || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Amount must be greater than zero");
        }
    }

    private Expense findExpense(Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Expense not found: " + id));
    }
}

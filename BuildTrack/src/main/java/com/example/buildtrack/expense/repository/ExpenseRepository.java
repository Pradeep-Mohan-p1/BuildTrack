
package com.example.buildtrack.expense.repository;

import com.example.buildtrack.expense.entity.Expense;
import com.example.buildtrack.expense.entity.ExpenseStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository
        extends JpaRepository<Expense, Long> {

    List<Expense> findByProjectId(Long projectId);

    List<Expense> findByCategory(
            com.example.buildtrack.expense.entity.ExpenseCategory category);

    List<Expense> findByStatus(ExpenseStatus status);

    List<Expense> findByExpenseDateBetween(
            LocalDate startDate, LocalDate endDate);

    List<Expense> findByProjectIdAndExpenseDateBetween(
            Long projectId, LocalDate startDate, LocalDate endDate);
}


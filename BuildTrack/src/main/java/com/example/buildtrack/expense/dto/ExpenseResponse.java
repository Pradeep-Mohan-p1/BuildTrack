package com.example.buildtrack.expense.dto;

import com.example.buildtrack.expense.entity.ExpenseCategory;
import com.example.buildtrack.expense.entity.ExpenseStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseResponse {

    private Long id;

    private Long projectId;

    private String projectName;

    private String description;

    private ExpenseCategory category;

    private BigDecimal amount;

    private LocalDate expenseDate;

    private String vendorName;

    private String invoiceNumber;

    private ExpenseStatus status;
}

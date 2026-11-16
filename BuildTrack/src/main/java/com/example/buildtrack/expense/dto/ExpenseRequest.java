package com.example.buildtrack.expense.dto;

import com.example.buildtrack.expense.entity.ExpenseCategory;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseRequest {

    private Long projectId;

    private String description;

    private ExpenseCategory category;

    private BigDecimal amount;

    private LocalDate expenseDate;

    private String vendorName;

    private String invoiceNumber;
}

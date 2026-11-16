package com.example.buildtrack.payroll.repository;

import com.example.buildtrack.payroll.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByPayrollId(Long payrollId);

    boolean existsByPaymentReference(String paymentReference);
}
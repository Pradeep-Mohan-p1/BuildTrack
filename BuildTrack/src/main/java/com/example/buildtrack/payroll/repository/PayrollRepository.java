package com.example.buildtrack.payroll.repository;

import com.example.buildtrack.payroll.entity.Payroll;
import com.example.buildtrack.payroll.entity.PayrollStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PayrollRepository extends JpaRepository<Payroll, Long> {

    Optional<Payroll> findByEmployeeIdAndMonthAndYear(
            Long employeeId, Integer month, Integer year);

    List<Payroll> findByEmployeeId(Long employeeId);

    List<Payroll> findByMonthAndYear(Integer month, Integer year);

    List<Payroll> findByStatus(PayrollStatus status);

    long countByStatus(PayrollStatus status);
}
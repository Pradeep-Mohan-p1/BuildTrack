package com.example.buildtrack.employee.repository;

import com.example.buildtrack.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository
        extends JpaRepository<Employee, Long> {

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Optional<Employee> findByEmail(String email);

    List<Employee> findByDepartmentIgnoreCase(String department);

    List<Employee> findByNameContainingIgnoreCase(String name);

    List<Employee> findByActiveTrue();

    List<Employee> findByActiveFalse();
}

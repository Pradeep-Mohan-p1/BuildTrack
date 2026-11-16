package com.example.buildtrack.employee.service;

import com.example.buildtrack.employee.dto.EmployeeRequest;
import com.example.buildtrack.employee.dto.EmployeeResponse;
import com.example.buildtrack.employee.entity.Employee;
import com.example.buildtrack.employee.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    // Convert Entity to Response DTO
    private EmployeeResponse mapToResponse(Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .name(employee.getName())
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .designation(employee.getDesignation())
                .department(employee.getDepartment())
                .dailyWage(employee.getDailyWage())
                .monthlySalary(employee.getMonthlySalary())
                .joiningDate(employee.getJoiningDate())
                .active(employee.getActive())
                .build();
    }

    // Add Employee
    public EmployeeResponse addEmployee(EmployeeRequest request) {

        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Email already exists");
        }

        if (employeeRepository.existsByPhone(request.getPhone())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Phone already exists");
        }

        Employee employee = Employee.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .designation(request.getDesignation())
                .department(request.getDepartment())
                .dailyWage(request.getDailyWage())
                .monthlySalary(request.getMonthlySalary())
                .joiningDate(request.getJoiningDate())
                .active(true)
                .build();

        Employee saved = employeeRepository.save(employee);

        return mapToResponse(saved);
    }

    // Get All Employees
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAllEmployees() {

        return employeeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Get Employee By ID
    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(Long id) {

        Employee employee = findEmployee(id);

        return mapToResponse(employee);
    }

    // Update Employee
    public EmployeeResponse updateEmployee(
            Long id, EmployeeRequest request) {

        Employee employee = findEmployee(id);

        if (employeeRepository.findByEmail(request.getEmail())
                .filter(existing -> !existing.getId().equals(id))
                .isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Email already exists");
        }

        if (employeeRepository.existsByPhone(request.getPhone())
                && !employee.getPhone().equals(request.getPhone())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Phone already exists");
        }

        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setDesignation(request.getDesignation());
        employee.setDepartment(request.getDepartment());
        employee.setDailyWage(request.getDailyWage());
        employee.setMonthlySalary(request.getMonthlySalary());
        employee.setJoiningDate(request.getJoiningDate());

        return mapToResponse(employeeRepository.save(employee));
    }

    // Deactivate Employee
    public EmployeeResponse deactivateEmployee(Long id) {

        Employee employee = findEmployee(id);

        employee.setActive(false);

        return mapToResponse(employeeRepository.save(employee));
    }

    // Activate Employee
    public EmployeeResponse activateEmployee(Long id) {

        Employee employee = findEmployee(id);

        employee.setActive(true);

        return mapToResponse(employeeRepository.save(employee));
    }

    // Search Employees By Name
    @Transactional(readOnly = true)
    public List<EmployeeResponse> searchEmployees(String name) {

        return employeeRepository
                .findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Filter Employees By Department
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getEmployeesByDepartment(
            String department) {

        return employeeRepository
                .findByDepartmentIgnoreCase(department)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Get Active Employees
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getActiveEmployees() {

        return employeeRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Get Inactive Employees
    @Transactional(readOnly = true)
    public List<EmployeeResponse> getInactiveEmployees() {

        return employeeRepository.findByActiveFalse()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Common method to find employee
    private Employee findEmployee(Long id) {

        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Employee not found with ID: " + id
                ));
    }
}

package com.example.buildtrack.employee.controller;

import com.example.buildtrack.employee.dto.EmployeeRequest;
import com.example.buildtrack.employee.dto.EmployeeResponse;
import com.example.buildtrack.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    // Add Employee
    @PostMapping
    public ResponseEntity<EmployeeResponse> addEmployee(
            @RequestBody EmployeeRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(employeeService.addEmployee(request));
    }

    // Get All Employees
    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {

        return ResponseEntity.ok(
                employeeService.getAllEmployees());
    }

    // Get Employee By ID
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getEmployeeById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                employeeService.getEmployeeById(id));
    }

    // Update Employee
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable Long id,
            @RequestBody EmployeeRequest request) {

        return ResponseEntity.ok(
                employeeService.updateEmployee(id, request));
    }

    // Deactivate Employee
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<EmployeeResponse> deactivateEmployee(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                employeeService.deactivateEmployee(id));
    }

    // Activate Employee
    @PatchMapping("/{id}/activate")
    public ResponseEntity<EmployeeResponse> activateEmployee(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                employeeService.activateEmployee(id));
    }

    // Search Employees
    @GetMapping("/search")
    public ResponseEntity<List<EmployeeResponse>> searchEmployees(
            @RequestParam String name) {

        return ResponseEntity.ok(
                employeeService.searchEmployees(name));
    }

    // Filter By Department
    @GetMapping("/department/{department}")
    public ResponseEntity<List<EmployeeResponse>>
            getEmployeesByDepartment(
                    @PathVariable String department) {

        return ResponseEntity.ok(
                employeeService.getEmployeesByDepartment(department));
    }

    // Get Active Employees
    @GetMapping("/active")
    public ResponseEntity<List<EmployeeResponse>> getActiveEmployees() {

        return ResponseEntity.ok(
                employeeService.getActiveEmployees());
    }

    // Get Inactive Employees
    @GetMapping("/inactive")
    public ResponseEntity<List<EmployeeResponse>> getInactiveEmployees() {

        return ResponseEntity.ok(
                employeeService.getInactiveEmployees());
    }
}

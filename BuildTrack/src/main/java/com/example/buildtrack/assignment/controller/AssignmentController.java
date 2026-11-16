
package com.example.buildtrack.assignment.controller;

import com.example.buildtrack.assignment.dto.AssignmentRequest;
import com.example.buildtrack.assignment.dto.AssignmentResponse;
import com.example.buildtrack.assignment.service.AssignmentService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/assignments")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    @PostMapping
    public ResponseEntity<AssignmentResponse> assignEmployee(
            @RequestBody AssignmentRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(assignmentService.assignEmployee(request));
    }

    @GetMapping
    public ResponseEntity<List<AssignmentResponse>> getAllAssignments() {
        return ResponseEntity.ok(
                assignmentService.getAllAssignments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssignmentResponse> getAssignmentById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                assignmentService.getAssignmentById(id));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<AssignmentResponse>> getEmployeeAssignments(
            @PathVariable Long employeeId) {

        return ResponseEntity.ok(
                assignmentService.getEmployeeAssignments(employeeId));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<AssignmentResponse>> getProjectAssignments(
            @PathVariable Long projectId) {

        return ResponseEntity.ok(
                assignmentService.getProjectAssignments(projectId));
    }

    @GetMapping("/site/{siteId}")
    public ResponseEntity<List<AssignmentResponse>> getSiteAssignments(
            @PathVariable Long siteId) {

        return ResponseEntity.ok(
                assignmentService.getSiteAssignments(siteId));
    }

    @GetMapping("/active")
    public ResponseEntity<List<AssignmentResponse>> getActiveAssignments() {
        return ResponseEntity.ok(
                assignmentService.getActiveAssignments());
    }

    @PatchMapping("/{id}/end")
    public ResponseEntity<AssignmentResponse> endAssignment(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                assignmentService.endAssignment(id));
    }
}

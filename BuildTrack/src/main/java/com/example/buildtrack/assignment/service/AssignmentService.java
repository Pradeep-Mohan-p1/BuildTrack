package com.example.buildtrack.assignment.service;

import com.example.buildtrack.assignment.dto.AssignmentRequest;
import com.example.buildtrack.assignment.dto.AssignmentResponse;
import com.example.buildtrack.assignment.entity.EmployeeProjectAssignment;
import com.example.buildtrack.assignment.repository.AssignmentRepository;

import com.example.buildtrack.employee.entity.Employee;
import com.example.buildtrack.employee.repository.EmployeeRepository;

import com.example.buildtrack.project.entity.Project;
import com.example.buildtrack.project.repository.ProjectRepository;

import com.example.buildtrack.site.entity.ConstructionSite;
import com.example.buildtrack.site.repository.SiteRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final EmployeeRepository employeeRepository;
    private final ProjectRepository projectRepository;
    private final SiteRepository siteRepository;

    private AssignmentResponse mapToResponse(
            EmployeeProjectAssignment assignment) {

        return AssignmentResponse.builder()
                .id(assignment.getId())
                .employeeId(assignment.getEmployee().getId())
                .employeeName(assignment.getEmployee().getName())
                .projectId(assignment.getProject().getId())
                .siteId(assignment.getSite().getId())
                .assignedDate(assignment.getAssignedDate())
                .endDate(assignment.getEndDate())
                .active(assignment.getActive())
                .build();
    }

    public AssignmentResponse assignEmployee(AssignmentRequest request) {

        Employee employee = employeeRepository.findById(
                request.getEmployeeId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Employee not found"));

        Project project = projectRepository.findById(
                request.getProjectId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Project not found"));

        ConstructionSite site = siteRepository.findById(
                request.getSiteId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Site not found"));

        if (!Boolean.TRUE.equals(employee.getActive())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cannot assign an inactive employee");
        }

        if (assignmentRepository
                .existsByEmployeeIdAndProjectIdAndSiteIdAndActiveTrue(
                        employee.getId(), project.getId(), site.getId())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Employee is already assigned to this site and project");
        }

        LocalDate date = request.getAssignedDate() == null
                ? LocalDate.now()
                : request.getAssignedDate();

        if (date.isAfter(LocalDate.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Assignment date cannot be in the future");
        }

        EmployeeProjectAssignment assignment =
                EmployeeProjectAssignment.builder()
                        .employee(employee)
                        .project(project)
                        .site(site)
                        .assignedDate(date)
                        .active(true)
                        .build();

        return mapToResponse(assignmentRepository.save(assignment));
    }

    @Transactional(readOnly = true)
    public List<AssignmentResponse> getAllAssignments() {

        return assignmentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public AssignmentResponse getAssignmentById(Long id) {

        return mapToResponse(findAssignment(id));
    }

    @Transactional(readOnly = true)
    public List<AssignmentResponse> getEmployeeAssignments(
            Long employeeId) {

        return assignmentRepository.findByEmployeeId(employeeId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AssignmentResponse> getProjectAssignments(
            Long projectId) {

        return assignmentRepository.findByProjectId(projectId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AssignmentResponse> getSiteAssignments(Long siteId) {

        return assignmentRepository.findBySiteId(siteId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AssignmentResponse> getActiveAssignments() {

        return assignmentRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public AssignmentResponse endAssignment(Long id) {

        EmployeeProjectAssignment assignment = findAssignment(id);

        if (!Boolean.TRUE.equals(assignment.getActive())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Assignment is already inactive");
        }

        assignment.setActive(false);
        assignment.setEndDate(LocalDate.now());

        return mapToResponse(assignmentRepository.save(assignment));
    }

    private EmployeeProjectAssignment findAssignment(Long id) {

        return assignmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Assignment not found: " + id));
    }
}

package com.example.buildtrack.assignment.repository;

import com.example.buildtrack.assignment.entity.EmployeeProjectAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssignmentRepository
        extends JpaRepository<EmployeeProjectAssignment, Long> {

    long countByActiveTrue();

    boolean existsByEmployeeIdAndProjectIdAndSiteIdAndActiveTrue(
            Long employeeId, Long projectId, Long siteId);

    List<EmployeeProjectAssignment> findByEmployeeId(Long employeeId);

    List<EmployeeProjectAssignment> findByProjectId(Long projectId);

    List<EmployeeProjectAssignment> findBySiteId(Long siteId);

    List<EmployeeProjectAssignment> findByActiveTrue();
}

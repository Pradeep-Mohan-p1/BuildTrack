package com.example.buildtrack.project.service;

import com.example.buildtrack.project.dto.ProjectRequest;
import com.example.buildtrack.project.dto.ProjectResponse;
import com.example.buildtrack.project.entity.Project;
import com.example.buildtrack.project.entity.ProjectStatus;
import com.example.buildtrack.project.repository.ProjectRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectService {

    private final ProjectRepository projectRepository;

    private ProjectResponse mapToResponse(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .projectName(project.getProjectName())
                .description(project.getDescription())
                .clientName(project.getClientName())
                .location(project.getLocation())
                .budget(project.getBudget())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .progress(project.getProgress())
                .status(project.getStatus())
                .build();
    }

    // Create project
    public ProjectResponse createProject(ProjectRequest request) {

        validateRequest(request);

        Project project = Project.builder()
                .projectName(request.getProjectName())
                .description(request.getDescription())
                .clientName(request.getClientName())
                .location(request.getLocation())
                .budget(request.getBudget())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .progress(0.0)
                .status(ProjectStatus.PLANNED)
                .build();

        return mapToResponse(projectRepository.save(project));
    }

    // Get all projects
    @Transactional(readOnly = true)
    public List<ProjectResponse> getAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Get project by ID
    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(Long id) {
        return mapToResponse(findProject(id));
    }

    // Update project
    public ProjectResponse updateProject(
            Long id, ProjectRequest request) {

        validateRequest(request);

        Project project = findProject(id);

        project.setProjectName(request.getProjectName());
        project.setDescription(request.getDescription());
        project.setClientName(request.getClientName());
        project.setLocation(request.getLocation());
        project.setBudget(request.getBudget());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());

        return mapToResponse(projectRepository.save(project));
    }

    // Update project status
    public ProjectResponse updateProjectStatus(
            Long id, ProjectStatus status) {

        if (status == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Status is required");
        }

        Project project = findProject(id);
        project.setStatus(status);

        return mapToResponse(projectRepository.save(project));
    }

    // Update project progress
    public ProjectResponse updateProjectProgress(
            Long id, Double progress) {

        if (progress == null || progress < 0 || progress > 100) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Progress must be between 0 and 100");
        }

        Project project = findProject(id);
        project.setProgress(progress);

        if (progress == 100) {
            project.setStatus(ProjectStatus.COMPLETED);
        } else if (progress > 0
                && project.getStatus() == ProjectStatus.PLANNED) {
            project.setStatus(ProjectStatus.ONGOING);
        }

        return mapToResponse(projectRepository.save(project));
    }

    // Search by project name
    @Transactional(readOnly = true)
    public List<ProjectResponse> searchProjects(String name) {

        return projectRepository
                .findByProjectNameContainingIgnoreCase(name)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Filter by status
    @Transactional(readOnly = true)
    public List<ProjectResponse> getProjectsByStatus(
            ProjectStatus status) {

        return projectRepository.findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Delete project
    public void deleteProject(Long id) {

        Project project = findProject(id);

        if (project.getStatus() == ProjectStatus.ONGOING) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cannot delete an ongoing project");
        }

        projectRepository.delete(project);
    }

    private void validateRequest(ProjectRequest request) {

        if (request.getProjectName() == null
                || request.getProjectName().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Project name is required");
        }

        if (request.getBudget() == null
                || request.getBudget().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Budget must be greater than zero");
        }

        if (request.getStartDate() != null
                && request.getEndDate() != null
                && request.getEndDate().isBefore(request.getStartDate())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "End date cannot be before start date");
        }
    }

    private Project findProject(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Project not found: " + id));
    }
}

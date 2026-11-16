package com.example.buildtrack.project.controller;

import com.example.buildtrack.project.dto.ProjectRequest;
import com.example.buildtrack.project.dto.ProjectResponse;
import com.example.buildtrack.project.entity.ProjectStatus;
import com.example.buildtrack.project.service.ProjectService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @RequestBody ProjectRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(projectService.createProject(request));
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProjectById(
            @PathVariable Long id) {

        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long id,
            @RequestBody ProjectRequest request) {

        return ResponseEntity.ok(
                projectService.updateProject(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ProjectResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam ProjectStatus status) {

        return ResponseEntity.ok(
                projectService.updateProjectStatus(id, status));
    }

    @PatchMapping("/{id}/progress")
    public ResponseEntity<ProjectResponse> updateProgress(
            @PathVariable Long id,
            @RequestBody Map<String, Double> request) {

        return ResponseEntity.ok(
                projectService.updateProjectProgress(
                        id, request.get("progress")));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProjectResponse>> searchProjects(
            @RequestParam String name) {

        return ResponseEntity.ok(
                projectService.searchProjects(name));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ProjectResponse>> getByStatus(
            @PathVariable ProjectStatus status) {

        return ResponseEntity.ok(
                projectService.getProjectsByStatus(status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable Long id) {

        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}

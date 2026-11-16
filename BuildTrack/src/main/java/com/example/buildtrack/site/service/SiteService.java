package com.example.buildtrack.site.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.buildtrack.project.entity.Project;
import com.example.buildtrack.project.entity.ProjectStatus;
import com.example.buildtrack.project.repository.ProjectRepository;
import com.example.buildtrack.site.dto.SiteRequest;
import com.example.buildtrack.site.dto.SiteResponse;
import com.example.buildtrack.site.entity.ConstructionSite;
import com.example.buildtrack.site.entity.SiteStatus;
import com.example.buildtrack.site.repository.SiteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class SiteService {

    private final SiteRepository siteRepository;
    private final ProjectRepository projectRepository;

    public SiteResponse createSite(SiteRequest request) {

        validateDates(request);

        Project project = getProject(request.getProjectId());

        if (project.getStatus() == ProjectStatus.COMPLETED ||
            project.getStatus() == ProjectStatus.CANCELLED) {
            throw new RuntimeException(
                    "Cannot create a site for a completed or cancelled project");
        }

        if (siteRepository.existsBySiteNameIgnoreCaseAndProjectId(
                request.getSiteName(), request.getProjectId())) {
            throw new RuntimeException(
                    "Site already exists under this project");
        }

        ConstructionSite site = ConstructionSite.builder()
                .siteName(request.getSiteName())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .project(project)
                .status(SiteStatus.ACTIVE)
                .build();

        return mapToResponse(siteRepository.save(site));
    }

    @Transactional(readOnly = true)
    public List<SiteResponse> getAllSites() {
        return siteRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SiteResponse getSiteById(Long id) {
        return mapToResponse(getSiteEntity(id));
    }

    @Transactional(readOnly = true)
    public List<SiteResponse> getSitesByProject(Long projectId) {
        return siteRepository.findByProjectId(projectId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SiteResponse> getSitesByStatus(SiteStatus status) {
        return siteRepository.findByStatus(status)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SiteResponse> getSitesByCity(String city) {
        return siteRepository.findByCityIgnoreCase(city)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public SiteResponse updateSite(Long id, SiteRequest request) {

        validateDates(request);

        ConstructionSite site = getSiteEntity(id);
        Project project = getProject(request.getProjectId());

        if (project.getStatus() == ProjectStatus.COMPLETED ||
            project.getStatus() == ProjectStatus.CANCELLED) {
            throw new RuntimeException(
                    "Cannot assign a site to a completed or cancelled project");
        }

        boolean duplicate = siteRepository
                .existsBySiteNameIgnoreCaseAndProjectId(
                        request.getSiteName(), request.getProjectId());

        if (duplicate &&
            !(site.getSiteName().equalsIgnoreCase(request.getSiteName()) &&
              site.getProject().getId().equals(request.getProjectId()))) {
            throw new RuntimeException("Site name already exists in this project");
        }

        site.setSiteName(request.getSiteName());
        site.setAddress(request.getAddress());
        site.setCity(request.getCity());
        site.setState(request.getState());
        site.setPincode(request.getPincode());
        site.setStartDate(request.getStartDate());
        site.setEndDate(request.getEndDate());
        site.setProject(project);

        return mapToResponse(siteRepository.save(site));
    }

    public SiteResponse updateSiteStatus(Long id, SiteStatus status) {

        ConstructionSite site = getSiteEntity(id);

        if (status == SiteStatus.ACTIVE) {
            ProjectStatus projectStatus = site.getProject().getStatus();

            if (projectStatus == ProjectStatus.COMPLETED ||
                projectStatus == ProjectStatus.CANCELLED) {
                throw new RuntimeException(
                        "Cannot activate a site under a completed project");
            }
        }

        site.setStatus(status);

        return mapToResponse(siteRepository.save(site));
    }

    public SiteResponse deactivateSite(Long id) {
        return updateSiteStatus(id, SiteStatus.INACTIVE);
    }

    private Project getProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new RuntimeException("Project not found: " + projectId));
    }

    public ConstructionSite getSiteEntity(Long id) {
        return siteRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Site not found: " + id));
    }

    private void validateDates(SiteRequest request) {
        if (request.getStartDate() != null &&
            request.getEndDate() != null &&
            request.getEndDate().isBefore(request.getStartDate())) {
            throw new RuntimeException(
                    "End date cannot be before start date");
        }
    }

    private SiteResponse mapToResponse(ConstructionSite site) {

        return SiteResponse.builder()
                .id(site.getId())
                .siteName(site.getSiteName())
                .address(site.getAddress())
                .city(site.getCity())
                .state(site.getState())
                .pincode(site.getPincode())
                .projectId(site.getProject().getId())
                .projectName(site.getProject().getProjectName())
                .startDate(site.getStartDate())
                .endDate(site.getEndDate())
                .status(site.getStatus())
                .build();
    }
}
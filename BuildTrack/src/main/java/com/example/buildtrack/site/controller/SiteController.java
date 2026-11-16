package com.example.buildtrack.site.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.buildtrack.site.dto.SiteRequest;
import com.example.buildtrack.site.dto.SiteResponse;
import com.example.buildtrack.site.entity.SiteStatus;
import com.example.buildtrack.site.service.SiteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/sites")
@RequiredArgsConstructor
public class SiteController {

    private final SiteService siteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SiteResponse createSite(
            @Valid @RequestBody SiteRequest request) {
        return siteService.createSite(request);
    }

    @GetMapping
    public List<SiteResponse> getAllSites() {
        return siteService.getAllSites();
    }

    @GetMapping("/{id}")
    public SiteResponse getSiteById(@PathVariable Long id) {
        return siteService.getSiteById(id);
    }

    @GetMapping("/project/{projectId}")
    public List<SiteResponse> getSitesByProject(
            @PathVariable Long projectId) {
        return siteService.getSitesByProject(projectId);
    }

    @GetMapping("/status/{status}")
    public List<SiteResponse> getSitesByStatus(
            @PathVariable SiteStatus status) {
        return siteService.getSitesByStatus(status);
    }

    @GetMapping("/city/{city}")
    public List<SiteResponse> getSitesByCity(
            @PathVariable String city) {
        return siteService.getSitesByCity(city);
    }

    @PutMapping("/{id}")
    public SiteResponse updateSite(
            @PathVariable Long id,
            @Valid @RequestBody SiteRequest request) {
        return siteService.updateSite(id, request);
    }

    @PatchMapping("/{id}/status")
    public SiteResponse updateSiteStatus(
            @PathVariable Long id,
            @RequestParam SiteStatus status) {
        return siteService.updateSiteStatus(id, status);
    }

    @PatchMapping("/{id}/deactivate")
    public SiteResponse deactivateSite(@PathVariable Long id) {
        return siteService.deactivateSite(id);
    }
}

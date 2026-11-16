package com.example.buildtrack.site.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.buildtrack.site.entity.ConstructionSite;
import com.example.buildtrack.site.entity.SiteStatus;

public interface SiteRepository
        extends JpaRepository<ConstructionSite, Long> {

    List<ConstructionSite> findByProjectId(Long projectId);

    List<ConstructionSite> findByStatus(SiteStatus status);

    List<ConstructionSite> findByCityIgnoreCase(String city);

    List<ConstructionSite> findByProjectIdAndStatus(
            Long projectId, SiteStatus status);

    boolean existsBySiteNameIgnoreCaseAndProjectId(
            String siteName, Long projectId);
}
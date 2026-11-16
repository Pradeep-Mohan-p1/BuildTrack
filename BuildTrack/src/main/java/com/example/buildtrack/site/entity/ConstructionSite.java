package com.example.buildtrack.site.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.buildtrack.project.entity.Project;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "construction_sites")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConstructionSite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String siteName;

    @Column(nullable = false, length = 500)
    private String address;

    private String city;

    private String state;

    private String pincode;

    private LocalDate startDate;

    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private SiteStatus status = SiteStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
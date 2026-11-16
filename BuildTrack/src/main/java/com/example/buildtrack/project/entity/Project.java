
package com.example.buildtrack.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "projects")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String projectName;

    private String description;

    private String clientName;

    private String location;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal budget;

    private LocalDate startDate;

    private LocalDate endDate;

    @Builder.Default
    private Double progress = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ProjectStatus status = ProjectStatus.PLANNED;
}

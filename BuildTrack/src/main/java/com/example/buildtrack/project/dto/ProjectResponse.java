
package com.example.buildtrack.project.dto;

import com.example.buildtrack.project.entity.ProjectStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {

    private Long id;

    private String projectName;

    private String description;

    private String clientName;

    private String location;

    private BigDecimal budget;

    private LocalDate startDate;

    private LocalDate endDate;

    private Double progress;

    private ProjectStatus status;
}


package com.example.buildtrack.project.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequest {

    private String projectName;

    private String description;

    private String clientName;

    private String location;

    private BigDecimal budget;

    private LocalDate startDate;

    private LocalDate endDate;
}

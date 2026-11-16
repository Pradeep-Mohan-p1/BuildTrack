
package com.example.buildtrack.assignment.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentResponse {

    private Long id;

    private Long employeeId;

    private String employeeName;

    private Long projectId;

    private Long siteId;

    private LocalDate assignedDate;

    private LocalDate endDate;

    private Boolean active;
}


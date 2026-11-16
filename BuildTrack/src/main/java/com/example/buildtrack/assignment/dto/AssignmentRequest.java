
package com.example.buildtrack.assignment.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentRequest {

    private Long employeeId;

    private Long projectId;

    private Long siteId;

    private LocalDate assignedDate;
}


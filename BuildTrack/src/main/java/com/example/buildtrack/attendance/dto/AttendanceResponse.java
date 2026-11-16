
package com.example.buildtrack.attendance.dto;

import com.example.buildtrack.attendance.entity.AttendanceStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponse {

    private Long id;

    private Long employeeId;

    private String employeeName;

    private Long siteId;

    private LocalDate attendanceDate;

    private AttendanceStatus status;

    private Double overtimeHours;

    private String remarks;
}

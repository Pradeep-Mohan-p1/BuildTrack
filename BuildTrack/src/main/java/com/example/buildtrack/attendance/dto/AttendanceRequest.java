
package com.example.buildtrack.attendance.dto;

import com.example.buildtrack.attendance.entity.AttendanceStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRequest {

    private Long employeeId;

    private Long siteId;

    private LocalDate attendanceDate;

    private AttendanceStatus status;

    private Double overtimeHours;

    private String remarks;
}

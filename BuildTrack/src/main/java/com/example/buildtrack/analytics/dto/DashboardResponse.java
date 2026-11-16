
package com.example.buildtrack.analytics.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    private long totalEmployees;

    private long activeEmployees;

    private long inactiveEmployees;

    private long totalAssignments;

    private long activeAssignments;

    private long totalAttendanceToday;

    private long presentToday;

    private long absentToday;

    private long leaveToday;

    private long halfDayToday;

    private double attendancePercentage;
}

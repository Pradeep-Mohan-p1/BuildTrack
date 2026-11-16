
package com.example.buildtrack.analytics.controller;

import com.example.buildtrack.analytics.dto.DashboardResponse;
import com.example.buildtrack.analytics.service.AnalyticsService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    // Complete dashboard
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponse> getDashboard() {

        return ResponseEntity.ok(
                analyticsService.getDashboardSummary());
    }

    // Total employees
    @GetMapping("/employees/count")
    public ResponseEntity<Long> getTotalEmployees() {

        return ResponseEntity.ok(
                analyticsService.getTotalEmployees());
    }

    // Active assignments
    @GetMapping("/assignments/active/count")
    public ResponseEntity<Long> getActiveAssignments() {

        return ResponseEntity.ok(
                analyticsService.getActiveAssignments());
    }

    // Attendance count for a selected date
    @GetMapping("/attendance/count")
    public ResponseEntity<Long> getAttendanceCount(
            @RequestParam LocalDate date) {

        return ResponseEntity.ok(
                analyticsService.getTotalAttendanceByDate(date));
    }
}

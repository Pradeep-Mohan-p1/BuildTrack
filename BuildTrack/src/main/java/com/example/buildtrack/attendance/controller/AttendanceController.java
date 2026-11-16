
package com.example.buildtrack.attendance.controller;

import com.example.buildtrack.attendance.dto.AttendanceRequest;
import com.example.buildtrack.attendance.dto.AttendanceResponse;
import com.example.buildtrack.attendance.service.AttendanceService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/mark")
    public ResponseEntity<AttendanceResponse> markAttendance(
            @RequestBody AttendanceRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(attendanceService.markAttendance(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AttendanceResponse> updateAttendance(
            @PathVariable Long id,
            @RequestBody AttendanceRequest request) {

        return ResponseEntity.ok(
                attendanceService.updateAttendance(id, request));
    }

    @GetMapping
    public ResponseEntity<List<AttendanceResponse>> getAllAttendance() {

        return ResponseEntity.ok(
                attendanceService.getAllAttendance());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendanceResponse> getAttendanceById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                attendanceService.getAttendanceById(id));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<AttendanceResponse>> getEmployeeAttendance(
            @PathVariable Long employeeId) {

        return ResponseEntity.ok(
                attendanceService.getEmployeeAttendance(employeeId));
    }

    @GetMapping("/site/{siteId}")
    public ResponseEntity<List<AttendanceResponse>> getSiteDailyAttendance(
            @PathVariable Long siteId,
            @RequestParam LocalDate date) {

        return ResponseEntity.ok(
                attendanceService.getSiteDailyAttendance(siteId, date));
    }

    @GetMapping("/daily")
    public ResponseEntity<List<AttendanceResponse>> getDailyAttendance(
            @RequestParam LocalDate date) {

        return ResponseEntity.ok(
                attendanceService.getDailyAttendance(date));
    }

    @GetMapping("/monthly/{employeeId}")
    public ResponseEntity<Object> getMonthlySummary(
            @PathVariable Long employeeId,
            @RequestParam int month,
            @RequestParam int year) {

        return ResponseEntity.ok(
                attendanceService.getMonthlySummary(
                        employeeId, month, year));
    }
}


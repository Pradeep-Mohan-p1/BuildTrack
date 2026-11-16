
package com.example.buildtrack.analytics.service;

import com.example.buildtrack.analytics.dto.DashboardResponse;

import com.example.buildtrack.employee.entity.Employee;
import com.example.buildtrack.employee.repository.EmployeeRepository;

import com.example.buildtrack.assignment.repository.AssignmentRepository;

import com.example.buildtrack.attendance.entity.Attendance;
import com.example.buildtrack.attendance.entity.AttendanceStatus;
import com.example.buildtrack.attendance.repository.AttendanceRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyticsService {

    private final EmployeeRepository employeeRepository;

    private final AssignmentRepository assignmentRepository;

    private final AttendanceRepository attendanceRepository;

    public DashboardResponse getDashboardSummary() {

        LocalDate today = LocalDate.now();

        // Employee statistics
        List<Employee> employees = employeeRepository.findAll();

        long totalEmployees = employees.size();

        long activeEmployees = employees.stream()
                .filter(e -> Boolean.TRUE.equals(e.getActive()))
                .count();

        long inactiveEmployees = totalEmployees - activeEmployees;

        // Assignment statistics
        long totalAssignments = assignmentRepository.count();

        long activeAssignments =
                assignmentRepository.countByActiveTrue();

        // Today's attendance
        List<Attendance> todayRecords =
                attendanceRepository.findByAttendanceDate(today);

        long totalAttendance = todayRecords.size();

        long present = countStatus(
                todayRecords, AttendanceStatus.PRESENT);

        long absent = countStatus(
                todayRecords, AttendanceStatus.ABSENT);

        long leave = countStatus(
                todayRecords, AttendanceStatus.LEAVE);

        long halfDay = countStatus(
                todayRecords, AttendanceStatus.HALF_DAY);

        // Attendance percentage
        double attendancePercentage = totalAttendance == 0
                ? 0.0
                : ((present + halfDay * 0.5) / totalAttendance) * 100.0;

        return DashboardResponse.builder()
                .totalEmployees(totalEmployees)
                .activeEmployees(activeEmployees)
                .inactiveEmployees(inactiveEmployees)
                .totalAssignments(totalAssignments)
                .activeAssignments(activeAssignments)
                .totalAttendanceToday(totalAttendance)
                .presentToday(present)
                .absentToday(absent)
                .leaveToday(leave)
                .halfDayToday(halfDay)
                .attendancePercentage(
                        Math.round(attendancePercentage * 100.0) / 100.0)
                .build();
    }

    private long countStatus(
            List<Attendance> records,
            AttendanceStatus status) {

        return records.stream()
                .filter(a -> a.getStatus() == status)
                .count();
    }

    // Get employee count
    public long getTotalEmployees() {
        return employeeRepository.count();
    }

    // Get active assignment count
    public long getActiveAssignments() {
        return assignmentRepository.countByActiveTrue();
    }

    // Get attendance records for a selected date
    public long getTotalAttendanceByDate(LocalDate date) {
        return attendanceRepository.findByAttendanceDate(date).size();
    }
}

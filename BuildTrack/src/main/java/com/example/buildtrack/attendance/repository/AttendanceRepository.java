package com.example.buildtrack.attendance.repository;

import com.example.buildtrack.attendance.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository
        extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByEmployeeIdAndSiteIdAndAttendanceDate(
            Long employeeId, Long siteId, LocalDate date);

    List<Attendance> findByEmployeeIdOrderByAttendanceDateDesc(
            Long employeeId);

    List<Attendance> findBySiteIdAndAttendanceDate(
            Long siteId, LocalDate date);

    List<Attendance> findByAttendanceDate(LocalDate date);

    List<Attendance> findByEmployeeIdAndAttendanceDateBetween(
            Long employeeId, LocalDate startDate, LocalDate endDate);

    List<Attendance> findByAttendanceDateBetween(
            LocalDate startDate, LocalDate endDate);

    long countByStatusAndAttendanceDate(
            com.example.buildtrack.attendance.entity.AttendanceStatus status,
            LocalDate date);

    long countByEmployeeIdAndStatusAndAttendanceDateBetween(
            Long employeeId,
            com.example.buildtrack.attendance.entity.AttendanceStatus status,
            LocalDate startDate,
            LocalDate endDate);
}

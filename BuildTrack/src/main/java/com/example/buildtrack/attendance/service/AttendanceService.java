
package com.example.buildtrack.attendance.service;

import com.example.buildtrack.attendance.dto.AttendanceRequest;
import com.example.buildtrack.attendance.dto.AttendanceResponse;
import com.example.buildtrack.attendance.entity.Attendance;
import com.example.buildtrack.attendance.entity.AttendanceStatus;
import com.example.buildtrack.attendance.repository.AttendanceRepository;

import com.example.buildtrack.assignment.repository.AssignmentRepository;
import com.example.buildtrack.employee.entity.Employee;
import com.example.buildtrack.employee.repository.EmployeeRepository;
import com.example.buildtrack.site.entity.ConstructionSite;
import com.example.buildtrack.site.repository.SiteRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;
    private final SiteRepository siteRepository;
    private final AssignmentRepository assignmentRepository;

    private AttendanceResponse mapToResponse(Attendance attendance) {

        return AttendanceResponse.builder()
                .id(attendance.getId())
                .employeeId(attendance.getEmployee().getId())
                .employeeName(attendance.getEmployee().getName())
                .siteId(attendance.getSite().getId())
                .attendanceDate(attendance.getAttendanceDate())
                .status(attendance.getStatus())
                .overtimeHours(attendance.getOvertimeHours())
                .remarks(attendance.getRemarks())
                .build();
    }

    // Mark attendance
    public AttendanceResponse markAttendance(AttendanceRequest request) {

        LocalDate date = request.getAttendanceDate() == null
                ? LocalDate.now()
                : request.getAttendanceDate();

        if (date.isAfter(LocalDate.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Future attendance cannot be marked");
        }

        if (request.getStatus() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Attendance status is required");
        }

        Employee employee = employeeRepository.findById(
                request.getEmployeeId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Employee not found"));

        ConstructionSite site = siteRepository.findById(
                request.getSiteId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Site not found"));

        if (!Boolean.TRUE.equals(employee.getActive())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Inactive employee cannot be marked");
        }

        boolean assigned = assignmentRepository
                .existsByEmployeeIdAndProjectIdAndSiteIdAndActiveTrue(
                        employee.getId(),
                        site.getProject().getId(),
                        site.getId());

        if (!assigned) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Employee is not assigned to this site and project");
        }

        if (attendanceRepository
                .findByEmployeeIdAndSiteIdAndAttendanceDate(
                        employee.getId(), site.getId(), date)
                .isPresent()) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Attendance already marked for this employee and date");
        }

        double overtime = request.getOvertimeHours() == null
                ? 0.0
                : request.getOvertimeHours();

        if (!Double.isFinite(overtime) || overtime < 0 || overtime > 24) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Overtime must be between 0 and 24 hours");
        }

        Attendance attendance = Attendance.builder()
                .employee(employee)
                .site(site)
                .attendanceDate(date)
                .status(request.getStatus())
                .overtimeHours(overtime)
                .remarks(request.getRemarks())
                .createdAt(LocalDateTime.now())
                .build();

        return mapToResponse(attendanceRepository.save(attendance));
    }

    // Update attendance
    public AttendanceResponse updateAttendance(
            Long id, AttendanceRequest request) {

        Attendance attendance = findAttendance(id);

        if (request.getStatus() != null) {
            attendance.setStatus(request.getStatus());
        }

        if (request.getOvertimeHours() != null) {
            double overtime = request.getOvertimeHours();

            if (!Double.isFinite(overtime) || overtime < 0 || overtime > 24) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid overtime hours");
            }

            attendance.setOvertimeHours(overtime);
        }

        if (request.getRemarks() != null) {
            attendance.setRemarks(request.getRemarks());
        }

        return mapToResponse(attendanceRepository.save(attendance));
    }

    // Get attendance by ID
    @Transactional(readOnly = true)
    public AttendanceResponse getAttendanceById(Long id) {
        return mapToResponse(findAttendance(id));
    }

    // Get all attendance records
    @Transactional(readOnly = true)
    public List<AttendanceResponse> getAllAttendance() {

        return attendanceRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Get employee attendance history
    @Transactional(readOnly = true)
    public List<AttendanceResponse> getEmployeeAttendance(Long employeeId) {

        return attendanceRepository
                .findByEmployeeIdOrderByAttendanceDateDesc(employeeId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Get daily site attendance
    @Transactional(readOnly = true)
    public List<AttendanceResponse> getSiteDailyAttendance(
            Long siteId, LocalDate date) {

        return attendanceRepository.findBySiteIdAndAttendanceDate(
                        siteId, date)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Get attendance for a particular date
    @Transactional(readOnly = true)
    public List<AttendanceResponse> getDailyAttendance(LocalDate date) {

        return attendanceRepository.findByAttendanceDate(date)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Monthly employee attendance summary
    @Transactional(readOnly = true)
    public Object getMonthlySummary(Long employeeId, int month, int year) {

        if (month < 1 || month > 12 || year < 2000) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid month or year");
        }

        YearMonth yearMonth = YearMonth.of(year, month);

        List<Attendance> records =
                attendanceRepository.findByEmployeeIdAndAttendanceDateBetween(
                        employeeId,
                        yearMonth.atDay(1),
                        yearMonth.atEndOfMonth());

        long present = records.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.PRESENT)
                .count();

        long absent = records.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.ABSENT)
                .count();

        long halfDay = records.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.HALF_DAY)
                .count();

        long leave = records.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.LEAVE)
                .count();

        long holidays = records.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.HOLIDAY)
                .count();

        double overtime = records.stream()
                .mapToDouble(Attendance::getOvertimeHours)
                .sum();

        return java.util.Map.of(
                "employeeId", employeeId,
                "month", month,
                "year", year,
                "presentDays", present,
                "absentDays", absent,
                "halfDays", halfDay,
                "leaveDays", leave,
                "holidays", holidays,
                "overtimeHours", overtime,
                "totalRecords", records.size()
        );
    }

    private Attendance findAttendance(Long id) {

        return attendanceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Attendance not found: " + id));
    }
}


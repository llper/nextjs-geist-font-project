package com.company.timetracker.controller;

import com.company.timetracker.dto.TimeEntryDTO;
import com.company.timetracker.service.TimeEntryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/time-entries")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class TimeEntryController {

    private final TimeEntryService timeEntryService;

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'HR_MANAGER', 'ADMIN')")
    public ResponseEntity<List<TimeEntryDTO>> getMyTimeEntries(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String type,
            Authentication authentication) {
        log.info("Getting time entries for user: {} from {} to {}", authentication.getName(), startDate, endDate);
        List<TimeEntryDTO> entries = timeEntryService.getTimeEntriesByKeycloakId(
                authentication.getName(), startDate, endDate, type);
        return ResponseEntity.ok(entries);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'HR_MANAGER', 'ADMIN')")
    public ResponseEntity<Page<TimeEntryDTO>> getAllTimeEntries(
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) Long taskId,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            Pageable pageable) {
        log.info("Getting all time entries with filters - employeeId: {}, taskId: {}, projectId: {}", 
                employeeId, taskId, projectId);
        Page<TimeEntryDTO> entries = timeEntryService.getAllTimeEntries(
                employeeId, taskId, projectId, startDate, endDate, type, status, pageable);
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'HR_MANAGER', 'ADMIN')")
    public ResponseEntity<TimeEntryDTO> getTimeEntry(@PathVariable Long id, Authentication authentication) {
        log.info("Getting time entry {} for user: {}", id, authentication.getName());
        TimeEntryDTO entry = timeEntryService.getTimeEntryById(id, authentication.getName());
        return ResponseEntity.ok(entry);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'HR_MANAGER', 'ADMIN')")
    public ResponseEntity<TimeEntryDTO> createTimeEntry(
            @Valid @RequestBody TimeEntryDTO timeEntryDTO, 
            Authentication authentication) {
        log.info("Creating time entry for user: {}", authentication.getName());
        TimeEntryDTO createdEntry = timeEntryService.createTimeEntry(timeEntryDTO, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEntry);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'HR_MANAGER', 'ADMIN')")
    public ResponseEntity<TimeEntryDTO> updateTimeEntry(
            @PathVariable Long id,
            @Valid @RequestBody TimeEntryDTO timeEntryDTO,
            Authentication authentication) {
        log.info("Updating time entry {} for user: {}", id, authentication.getName());
        TimeEntryDTO updatedEntry = timeEntryService.updateTimeEntry(id, timeEntryDTO, authentication.getName());
        return ResponseEntity.ok(updatedEntry);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'HR_MANAGER', 'ADMIN')")
    public ResponseEntity<Void> deleteTimeEntry(@PathVariable Long id, Authentication authentication) {
        log.info("Deleting time entry {} for user: {}", id, authentication.getName());
        timeEntryService.deleteTimeEntry(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('MANAGER', 'HR_MANAGER', 'ADMIN')")
    public ResponseEntity<TimeEntryDTO> approveTimeEntry(@PathVariable Long id, Authentication authentication) {
        log.info("Approving time entry {} by user: {}", id, authentication.getName());
        TimeEntryDTO approvedEntry = timeEntryService.approveTimeEntry(id, authentication.getName());
        return ResponseEntity.ok(approvedEntry);
    }

    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('MANAGER', 'HR_MANAGER', 'ADMIN')")
    public ResponseEntity<TimeEntryDTO> rejectTimeEntry(@PathVariable Long id, Authentication authentication) {
        log.info("Rejecting time entry {} by user: {}", id, authentication.getName());
        TimeEntryDTO rejectedEntry = timeEntryService.rejectTimeEntry(id, authentication.getName());
        return ResponseEntity.ok(rejectedEntry);
    }

    @GetMapping("/my/summary")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'HR_MANAGER', 'ADMIN')")
    public ResponseEntity<TimeEntrySummaryDTO> getMyTimeEntrySummary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Authentication authentication) {
        log.info("Getting time entry summary for user: {} from {} to {}", authentication.getName(), startDate, endDate);
        TimeEntrySummaryDTO summary = timeEntryService.getTimeEntrySummary(authentication.getName(), startDate, endDate);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/reports/daily")
    @PreAuthorize("hasAnyRole('MANAGER', 'HR_MANAGER', 'ADMIN')")
    public ResponseEntity<List<DailyTimeReportDTO>> getDailyTimeReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Long employeeId) {
        log.info("Getting daily time report for date: {} and employeeId: {}", date, employeeId);
        List<DailyTimeReportDTO> report = timeEntryService.getDailyTimeReport(date, employeeId);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/reports/weekly")
    @PreAuthorize("hasAnyRole('MANAGER', 'HR_MANAGER', 'ADMIN')")
    public ResponseEntity<List<WeeklyTimeReportDTO>> getWeeklyTimeReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) Long employeeId) {
        log.info("Getting weekly time report starting from: {} for employeeId: {}", startDate, employeeId);
        List<WeeklyTimeReportDTO> report = timeEntryService.getWeeklyTimeReport(startDate, employeeId);
        return ResponseEntity.ok(report);
    }

    @PostMapping("/bulk")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'HR_MANAGER', 'ADMIN')")
    public ResponseEntity<List<TimeEntryDTO>> createBulkTimeEntries(
            @Valid @RequestBody List<TimeEntryDTO> timeEntries,
            Authentication authentication) {
        log.info("Creating {} time entries in bulk for user: {}", timeEntries.size(), authentication.getName());
        List<TimeEntryDTO> createdEntries = timeEntryService.createBulkTimeEntries(timeEntries, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEntries);
    }

    // Inner DTOs for reports and summaries
    public static class TimeEntrySummaryDTO {
        private double totalHours;
        private double presenceHours;
        private double taskHours;
        private int totalEntries;
        private Map<String, Double> hoursByProject;
        private Map<String, Double> hoursByTask;

        // Constructors, getters, and setters
        public TimeEntrySummaryDTO() {}

        public TimeEntrySummaryDTO(double totalHours, double presenceHours, double taskHours, 
                                  int totalEntries, Map<String, Double> hoursByProject, 
                                  Map<String, Double> hoursByTask) {
            this.totalHours = totalHours;
            this.presenceHours = presenceHours;
            this.taskHours = taskHours;
            this.totalEntries = totalEntries;
            this.hoursByProject = hoursByProject;
            this.hoursByTask = hoursByTask;
        }

        // Getters and setters
        public double getTotalHours() { return totalHours; }
        public void setTotalHours(double totalHours) { this.totalHours = totalHours; }
        
        public double getPresenceHours() { return presenceHours; }
        public void setPresenceHours(double presenceHours) { this.presenceHours = presenceHours; }
        
        public double getTaskHours() { return taskHours; }
        public void setTaskHours(double taskHours) { this.taskHours = taskHours; }
        
        public int getTotalEntries() { return totalEntries; }
        public void setTotalEntries(int totalEntries) { this.totalEntries = totalEntries; }
        
        public Map<String, Double> getHoursByProject() { return hoursByProject; }
        public void setHoursByProject(Map<String, Double> hoursByProject) { this.hoursByProject = hoursByProject; }
        
        public Map<String, Double> getHoursByTask() { return hoursByTask; }
        public void setHoursByTask(Map<String, Double> hoursByTask) { this.hoursByTask = hoursByTask; }
    }

    public static class DailyTimeReportDTO {
        private String employeeName;
        private LocalDate date;
        private double totalHours;
        private double presenceHours;
        private double taskHours;
        private List<TimeEntryDTO> entries;

        // Constructors, getters, and setters
        public DailyTimeReportDTO() {}

        public DailyTimeReportDTO(String employeeName, LocalDate date, double totalHours, 
                                 double presenceHours, double taskHours, List<TimeEntryDTO> entries) {
            this.employeeName = employeeName;
            this.date = date;
            this.totalHours = totalHours;
            this.presenceHours = presenceHours;
            this.taskHours = taskHours;
            this.entries = entries;
        }

        // Getters and setters
        public String getEmployeeName() { return employeeName; }
        public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
        
        public LocalDate getDate() { return date; }
        public void setDate(LocalDate date) { this.date = date; }
        
        public double getTotalHours() { return totalHours; }
        public void setTotalHours(double totalHours) { this.totalHours = totalHours; }
        
        public double getPresenceHours() { return presenceHours; }
        public void setPresenceHours(double presenceHours) { this.presenceHours = presenceHours; }
        
        public double getTaskHours() { return taskHours; }
        public void setTaskHours(double taskHours) { this.taskHours = taskHours; }
        
        public List<TimeEntryDTO> getEntries() { return entries; }
        public void setEntries(List<TimeEntryDTO> entries) { this.entries = entries; }
    }

    public static class WeeklyTimeReportDTO {
        private String employeeName;
        private LocalDate weekStartDate;
        private LocalDate weekEndDate;
        private double totalHours;
        private Map<LocalDate, Double> dailyHours;

        // Constructors, getters, and setters
        public WeeklyTimeReportDTO() {}

        public WeeklyTimeReportDTO(String employeeName, LocalDate weekStartDate, LocalDate weekEndDate, 
                                  double totalHours, Map<LocalDate, Double> dailyHours) {
            this.employeeName = employeeName;
            this.weekStartDate = weekStartDate;
            this.weekEndDate = weekEndDate;
            this.totalHours = totalHours;
            this.dailyHours = dailyHours;
        }

        // Getters and setters
        public String getEmployeeName() { return employeeName; }
        public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
        
        public LocalDate getWeekStartDate() { return weekStartDate; }
        public void setWeekStartDate(LocalDate weekStartDate) { this.weekStartDate = weekStartDate; }
        
        public LocalDate getWeekEndDate() { return weekEndDate; }
        public void setWeekEndDate(LocalDate weekEndDate) { this.weekEndDate = weekEndDate; }
        
        public double getTotalHours() { return totalHours; }
        public void setTotalHours(double totalHours) { this.totalHours = totalHours; }
        
        public Map<LocalDate, Double> getDailyHours() { return dailyHours; }
        public void setDailyHours(Map<LocalDate, Double> dailyHours) { this.dailyHours = dailyHours; }
    }
}

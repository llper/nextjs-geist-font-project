package com.company.timetracker.controller;

import com.company.timetracker.dto.VacationRequestDTO;
import com.company.timetracker.service.VacationRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/vacation-requests")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class VacationRequestController {

    private final VacationRequestService vacationRequestService;

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'HR_MANAGER', 'ADMIN')")
    public ResponseEntity<List<VacationRequestDTO>> getMyVacationRequests(Authentication authentication) {
        log.info("Getting vacation requests for user: {}", authentication.getName());
        List<VacationRequestDTO> requests = vacationRequestService.getVacationRequestsByKeycloakId(authentication.getName());
        return ResponseEntity.ok(requests);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'HR_MANAGER', 'ADMIN')")
    public ResponseEntity<Page<VacationRequestDTO>> getAllVacationRequests(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            Pageable pageable) {
        log.info("Getting all vacation requests with filters - status: {}, employeeId: {}, startDate: {}, endDate: {}", 
                status, employeeId, startDate, endDate);
        Page<VacationRequestDTO> requests = vacationRequestService.getAllVacationRequests(status, employeeId, startDate, endDate, pageable);
        return ResponseEntity.ok(requests);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'HR_MANAGER', 'ADMIN')")
    public ResponseEntity<VacationRequestDTO> getVacationRequest(@PathVariable Long id, Authentication authentication) {
        log.info("Getting vacation request {} for user: {}", id, authentication.getName());
        VacationRequestDTO request = vacationRequestService.getVacationRequestById(id, authentication.getName());
        return ResponseEntity.ok(request);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'HR_MANAGER', 'ADMIN')")
    public ResponseEntity<VacationRequestDTO> createVacationRequest(
            @Valid @RequestBody VacationRequestDTO vacationRequestDTO, 
            Authentication authentication) {
        log.info("Creating vacation request for user: {}", authentication.getName());
        VacationRequestDTO createdRequest = vacationRequestService.createVacationRequest(vacationRequestDTO, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRequest);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'HR_MANAGER', 'ADMIN')")
    public ResponseEntity<VacationRequestDTO> updateVacationRequest(
            @PathVariable Long id,
            @Valid @RequestBody VacationRequestDTO vacationRequestDTO,
            Authentication authentication) {
        log.info("Updating vacation request {} for user: {}", id, authentication.getName());
        VacationRequestDTO updatedRequest = vacationRequestService.updateVacationRequest(id, vacationRequestDTO, authentication.getName());
        return ResponseEntity.ok(updatedRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'HR_MANAGER', 'ADMIN')")
    public ResponseEntity<Void> deleteVacationRequest(@PathVariable Long id, Authentication authentication) {
        log.info("Deleting vacation request {} for user: {}", id, authentication.getName());
        vacationRequestService.deleteVacationRequest(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('MANAGER', 'HR_MANAGER', 'ADMIN')")
    public ResponseEntity<VacationRequestDTO> approveVacationRequest(@PathVariable Long id, Authentication authentication) {
        log.info("Approving vacation request {} by user: {}", id, authentication.getName());
        VacationRequestDTO approvedRequest = vacationRequestService.approveVacationRequest(id, authentication.getName());
        return ResponseEntity.ok(approvedRequest);
    }

    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('MANAGER', 'HR_MANAGER', 'ADMIN')")
    public ResponseEntity<VacationRequestDTO> rejectVacationRequest(
            @PathVariable Long id,
            @RequestBody(required = false) String rejectionReason,
            Authentication authentication) {
        log.info("Rejecting vacation request {} by user: {}", id, authentication.getName());
        VacationRequestDTO rejectedRequest = vacationRequestService.rejectVacationRequest(id, rejectionReason, authentication.getName());
        return ResponseEntity.ok(rejectedRequest);
    }

    @PatchMapping("/{id}/skip-approval")
    @PreAuthorize("hasAnyRole('HR_MANAGER', 'ADMIN')")
    public ResponseEntity<VacationRequestDTO> skipApproval(
            @PathVariable Long id,
            @RequestBody(required = false) String skipLabel,
            Authentication authentication) {
        log.info("Skipping approval for vacation request {} by user: {}", id, authentication.getName());
        VacationRequestDTO skippedRequest = vacationRequestService.skipApproval(id, skipLabel, authentication.getName());
        return ResponseEntity.ok(skippedRequest);
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('MANAGER', 'HR_MANAGER', 'ADMIN')")
    public ResponseEntity<List<VacationRequestDTO>> getPendingVacationRequests() {
        log.info("Getting pending vacation requests");
        List<VacationRequestDTO> pendingRequests = vacationRequestService.getPendingVacationRequests();
        return ResponseEntity.ok(pendingRequests);
    }

    @GetMapping("/calendar")
    @PreAuthorize("hasAnyRole('MANAGER', 'HR_MANAGER', 'ADMIN')")
    public ResponseEntity<List<VacationRequestDTO>> getVacationCalendar(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        log.info("Getting vacation calendar from {} to {}", startDate, endDate);
        List<VacationRequestDTO> vacations = vacationRequestService.getVacationCalendar(startDate, endDate);
        return ResponseEntity.ok(vacations);
    }

    @GetMapping("/my/summary")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'MANAGER', 'HR_MANAGER', 'ADMIN')")
    public ResponseEntity<VacationSummaryDTO> getMyVacationSummary(
            @RequestParam(defaultValue = "2024") int year,
            Authentication authentication) {
        log.info("Getting vacation summary for user: {} and year: {}", authentication.getName(), year);
        VacationSummaryDTO summary = vacationRequestService.getVacationSummary(authentication.getName(), year);
        return ResponseEntity.ok(summary);
    }

    // Inner DTO for vacation summary
    public static class VacationSummaryDTO {
        private int totalVacationDays;
        private int usedVacationDays;
        private int remainingVacationDays;
        private int pendingVacationDays;
        private List<VacationRequestDTO> upcomingVacations;

        // Constructors, getters, and setters
        public VacationSummaryDTO() {}

        public VacationSummaryDTO(int totalVacationDays, int usedVacationDays, int remainingVacationDays, 
                                 int pendingVacationDays, List<VacationRequestDTO> upcomingVacations) {
            this.totalVacationDays = totalVacationDays;
            this.usedVacationDays = usedVacationDays;
            this.remainingVacationDays = remainingVacationDays;
            this.pendingVacationDays = pendingVacationDays;
            this.upcomingVacations = upcomingVacations;
        }

        // Getters and setters
        public int getTotalVacationDays() { return totalVacationDays; }
        public void setTotalVacationDays(int totalVacationDays) { this.totalVacationDays = totalVacationDays; }
        
        public int getUsedVacationDays() { return usedVacationDays; }
        public void setUsedVacationDays(int usedVacationDays) { this.usedVacationDays = usedVacationDays; }
        
        public int getRemainingVacationDays() { return remainingVacationDays; }
        public void setRemainingVacationDays(int remainingVacationDays) { this.remainingVacationDays = remainingVacationDays; }
        
        public int getPendingVacationDays() { return pendingVacationDays; }
        public void setPendingVacationDays(int pendingVacationDays) { this.pendingVacationDays = pendingVacationDays; }
        
        public List<VacationRequestDTO> getUpcomingVacations() { return upcomingVacations; }
        public void setUpcomingVacations(List<VacationRequestDTO> upcomingVacations) { this.upcomingVacations = upcomingVacations; }
    }
}

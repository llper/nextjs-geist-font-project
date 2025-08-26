package com.company.timetracker.dto;

import com.company.timetracker.entity.VacationStatus;
import com.company.timetracker.entity.VacationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VacationRequestDTO {

    private Long id;

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @NotNull(message = "Start date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotNull(message = "Vacation type is required")
    private VacationType type;

    @Size(max = 500, message = "Reason must not exceed 500 characters")
    private String reason;

    private VacationStatus status;

    private String approvedBy;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime approvedAt;

    @Size(max = 500, message = "Rejection reason must not exceed 500 characters")
    private String rejectionReason;

    private Boolean approvalSkipped;

    @Size(max = 100, message = "Approval skip label must not exceed 100 characters")
    private String approvalSkipLabel;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    // Related entity information
    private String employeeName;
    private String employeeEmail;
    private String approverName;

    // Computed fields
    public int getDaysRequested() {
        if (startDate != null && endDate != null) {
            return (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
        }
        return 0;
    }

    public boolean isPending() {
        return status == VacationStatus.PENDING;
    }

    public boolean isApproved() {
        return status == VacationStatus.APPROVED;
    }

    public boolean isRejected() {
        return status == VacationStatus.REJECTED;
    }

    public boolean isInPast() {
        return startDate != null && startDate.isBefore(LocalDate.now());
    }

    public boolean overlaps(LocalDate otherStart, LocalDate otherEnd) {
        if (startDate == null || endDate == null || otherStart == null || otherEnd == null) {
            return false;
        }
        return !startDate.isAfter(otherEnd) && !endDate.isBefore(otherStart);
    }

    public String getStatusDisplay() {
        if (approvalSkipped != null && approvalSkipped) {
            return status.getDisplayName() + " (" + (approvalSkipLabel != null ? approvalSkipLabel : "Approval Skipped") + ")";
        }
        return status != null ? status.getDisplayName() : "";
    }
}

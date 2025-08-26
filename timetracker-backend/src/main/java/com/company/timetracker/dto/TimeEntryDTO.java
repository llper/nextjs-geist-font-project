package com.company.timetracker.dto;

import com.company.timetracker.entity.TimeEntryStatus;
import com.company.timetracker.entity.TimeEntryType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeEntryDTO {

    private Long id;

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    private Long taskId;

    @NotNull(message = "Entry type is required")
    private TimeEntryType type;

    @NotNull(message = "Entry date is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime entryDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;

    @NotNull(message = "Hours is required")
    @Positive(message = "Hours must be positive")
    private Double hours;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;

    private TimeEntryStatus status;

    private String approvedBy;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime approvedAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    // Related entity information
    private String employeeName;
    private String taskName;
    private String taskCode;
    private String projectName;
    private String projectCode;

    // Computed fields
    public boolean isPresenceEntry() {
        return type == TimeEntryType.PRESENCE;
    }

    public boolean isTaskEntry() {
        return type == TimeEntryType.TASK;
    }

    public boolean isApproved() {
        return status == TimeEntryStatus.APPROVED;
    }

    public boolean isPending() {
        return status == TimeEntryStatus.PENDING;
    }
}

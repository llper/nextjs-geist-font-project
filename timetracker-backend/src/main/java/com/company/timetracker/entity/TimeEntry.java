package com.company.timetracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "time_entries")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"employee", "task"})
@EqualsAndHashCode(of = {"id"})
public class TimeEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TimeEntryType type;

    @NotNull
    @Column(name = "entry_date", nullable = false)
    private LocalDateTime entryDate;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Positive
    @Column(name = "hours", nullable = false)
    private Double hours;

    @Size(max = 500)
    @Column(name = "description")
    private String description;

    @Size(max = 1000)
    @Column(name = "notes")
    private String notes;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TimeEntryStatus status = TimeEntryStatus.PENDING;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Utility methods
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

    public void approve(String approverKeycloakId) {
        this.status = TimeEntryStatus.APPROVED;
        this.approvedBy = approverKeycloakId;
        this.approvedAt = LocalDateTime.now();
    }

    public void reject() {
        this.status = TimeEntryStatus.REJECTED;
    }
}

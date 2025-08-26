package com.company.timetracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "vacation_requests")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"employee"})
@EqualsAndHashCode(of = {"id"})
public class VacationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private VacationType type;

    @Size(max = 500)
    @Column(name = "reason")
    private String reason;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private VacationStatus status = VacationStatus.PENDING;

    @Column(name = "approved_by")
    private String approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Size(max = 500)
    @Column(name = "rejection_reason")
    private String rejectionReason;

    @Builder.Default
    @Column(name = "approval_skipped", nullable = false)
    private Boolean approvalSkipped = false;

    @Size(max = 100)
    @Column(name = "approval_skip_label")
    private String approvalSkipLabel;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Utility methods
    public int getDaysRequested() {
        return (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
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

    public void approve(String approverKeycloakId) {
        this.status = VacationStatus.APPROVED;
        this.approvedBy = approverKeycloakId;
        this.approvedAt = LocalDateTime.now();
        this.approvalSkipped = false;
        this.approvalSkipLabel = null;
    }

    public void reject(String rejectionReason) {
        this.status = VacationStatus.REJECTED;
        this.rejectionReason = rejectionReason;
        this.approvalSkipped = false;
        this.approvalSkipLabel = null;
    }

    public void skipApproval(String skipLabel) {
        this.status = VacationStatus.APPROVED;
        this.approvalSkipped = true;
        this.approvalSkipLabel = skipLabel != null ? skipLabel : "Approval Skipped";
        this.approvedAt = LocalDateTime.now();
    }

    public boolean isInPast() {
        return startDate.isBefore(LocalDate.now());
    }

    public boolean overlaps(LocalDate otherStart, LocalDate otherEnd) {
        return !startDate.isAfter(otherEnd) && !endDate.isBefore(otherStart);
    }
}

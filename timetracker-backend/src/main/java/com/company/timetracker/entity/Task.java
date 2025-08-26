package com.company.timetracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tasks")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"timeEntries"})
@EqualsAndHashCode(of = {"id", "code"})
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(name = "code", nullable = false)
    private String code;

    @NotBlank
    @Size(max = 200)
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 1000)
    @Column(name = "description")
    private String description;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TaskStatus status = TaskStatus.OPEN;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private TaskPriority priority = TaskPriority.MEDIUM;

    @Column(name = "estimated_hours")
    private Double estimatedHours;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_employee_id")
    private Employee assignedEmployee;

    @Builder.Default
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TimeEntry> timeEntries = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Utility methods
    public String getFullCode() {
        return project != null ? project.getCode() + "-" + code : code;
    }

    public boolean isOpen() {
        return status == TaskStatus.OPEN;
    }

    public boolean isCompleted() {
        return status == TaskStatus.COMPLETED;
    }

    public boolean canAcceptTimeEntries() {
        return status == TaskStatus.OPEN || status == TaskStatus.IN_PROGRESS;
    }

    public Double getTotalLoggedHours() {
        return timeEntries.stream()
                .filter(entry -> entry.getType() == TimeEntryType.TASK)
                .mapToDouble(TimeEntry::getHours)
                .sum();
    }
}

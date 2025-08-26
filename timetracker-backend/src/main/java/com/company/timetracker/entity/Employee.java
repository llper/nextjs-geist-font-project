package com.company.timetracker.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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
@Table(name = "employees")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"timeEntries", "vacationRequests", "projects"})
@EqualsAndHashCode(of = {"id", "email"})
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank
    @Size(max = 100)
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Email
    @NotBlank
    @Size(max = 255)
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank
    @Size(max = 100)
    @Column(name = "keycloak_id", nullable = false, unique = true)
    private String keycloakId;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private EmployeeRole role = EmployeeRole.EMPLOYEE;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EmployeeStatus status = EmployeeStatus.ACTIVE;

    @Size(max = 100)
    @Column(name = "department")
    private String department;

    @Size(max = 100)
    @Column(name = "position")
    private String position;

    @Column(name = "hire_date")
    private LocalDateTime hireDate;

    @Builder.Default
    @Column(name = "vacation_days_per_year")
    private Integer vacationDaysPerYear = 25;

    @Builder.Default
    @Column(name = "remaining_vacation_days")
    private Integer remainingVacationDays = 25;

    @Builder.Default
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TimeEntry> timeEntries = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<VacationRequest> vacationRequests = new ArrayList<>();

    @Builder.Default
    @ManyToMany(mappedBy = "assignedEmployees", fetch = FetchType.LAZY)
    private List<Project> projects = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Utility methods
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean canApproveVacations() {
        return role == EmployeeRole.ADMIN || role == EmployeeRole.HR_MANAGER || role == EmployeeRole.MANAGER;
    }
}

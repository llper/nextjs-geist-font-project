package com.company.timetracker.entity;

public enum EmployeeRole {
    EMPLOYEE("Employee"),
    MANAGER("Manager"),
    HR_MANAGER("HR Manager"),
    ADMIN("Administrator");

    private final String displayName;

    EmployeeRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean canApproveVacations() {
        return this == ADMIN || this == HR_MANAGER || this == MANAGER;
    }

    public boolean canManageEmployees() {
        return this == ADMIN || this == HR_MANAGER;
    }

    public boolean canManageProjects() {
        return this == ADMIN || this == MANAGER;
    }
}

package com.company.timetracker.entity;

public enum ProjectStatus {
    ACTIVE("Active"),
    COMPLETED("Completed"),
    ON_HOLD("On Hold"),
    CANCELLED("Cancelled");

    private final String displayName;

    ProjectStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isActive() {
        return this == ACTIVE;
    }

    public boolean canAcceptTimeEntries() {
        return this == ACTIVE;
    }
}

package com.company.timetracker.entity;

public enum TaskStatus {
    OPEN("Open"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    ON_HOLD("On Hold");

    private final String displayName;

    TaskStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean canAcceptTimeEntries() {
        return this == OPEN || this == IN_PROGRESS;
    }

    public boolean isCompleted() {
        return this == COMPLETED;
    }
}

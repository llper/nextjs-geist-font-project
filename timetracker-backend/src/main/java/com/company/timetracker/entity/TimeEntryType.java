package com.company.timetracker.entity;

public enum TimeEntryType {
    PRESENCE("Presence"),
    TASK("Task Work");

    private final String displayName;

    TimeEntryType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean requiresTask() {
        return this == TASK;
    }
}

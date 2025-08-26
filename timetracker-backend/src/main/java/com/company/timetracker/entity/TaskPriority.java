package com.company.timetracker.entity;

public enum TaskPriority {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High"),
    URGENT("Urgent");

    private final String displayName;

    TaskPriority(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getPriorityLevel() {
        return switch (this) {
            case LOW -> 1;
            case MEDIUM -> 2;
            case HIGH -> 3;
            case URGENT -> 4;
        };
    }
}

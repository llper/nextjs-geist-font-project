package com.company.timetracker.entity;

public enum VacationType {
    ANNUAL_LEAVE("Annual Leave"),
    SICK_LEAVE("Sick Leave"),
    PERSONAL_LEAVE("Personal Leave"),
    MATERNITY_LEAVE("Maternity Leave"),
    PATERNITY_LEAVE("Paternity Leave"),
    UNPAID_LEAVE("Unpaid Leave"),
    COMPENSATORY_LEAVE("Compensatory Leave");

    private final String displayName;

    VacationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean requiresApproval() {
        return this != SICK_LEAVE; // Sick leave might not always require approval
    }

    public boolean isPaid() {
        return this != UNPAID_LEAVE;
    }
}

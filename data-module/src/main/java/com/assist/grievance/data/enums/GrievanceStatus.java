package com.assist.grievance.data.enums;

public enum GrievanceStatus {
    RECEIVED("received", "REC_001"),
    PROCESSED("processed", "PROC_002"),
    REJECTED("rejected", "REJ_003"),
    PENDING("pending", "PEND_004"),
    REJISTERED("registered", "REG_005");

    private final String status;
    private final String statusCode;

    GrievanceStatus(String status, String statusCode) {
        this.status = status;
        this.statusCode = statusCode;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    // Method to get enum from string
    public static GrievanceStatus fromString(String status) {
        for (GrievanceStatus gs : GrievanceStatus.values()) {
            if (gs.status.equalsIgnoreCase(status)) {
                return gs;
            }
        }
        throw new IllegalArgumentException("Invalid status: " + status);
    }
}

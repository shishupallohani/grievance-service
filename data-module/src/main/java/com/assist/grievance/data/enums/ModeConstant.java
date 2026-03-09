package com.assist.grievance.data.enums;

public enum ModeConstant {
    GMAIL("online", "online_001"),

    PORTAL("portal", "portal_002"),

    OFFLINE("offline", "offline_003"),

    MEMBER_PORTAL("member portal", "member_portal_004"),

    PROVIDER_PORTAL("provider portal", "provider_portal_005");

    private final String status;
    private final String statusCode;

    ModeConstant(String status, String statusCode) {
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
    public static ModeConstant fromString(String status) {
        for (ModeConstant mc : ModeConstant.values()) {
            if (mc.status.equalsIgnoreCase(status)) {
                return mc;
            }
        }
        throw new IllegalArgumentException("Invalid status: " + status);
    }

}

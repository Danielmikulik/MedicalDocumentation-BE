package com.dm.MedicalDocumentation.user;

public enum Role {
    ADMIN("admin"),
    PATIENT("patient"),
    DOCTOR("doctor"),
    HOSPITAL("hospital");

    private final String role;
    Role(String role) {
        this.role = role;
    }

    public static Role fromString(String value) {
        for (Role role : Role.values()) {
            if (role.role.equalsIgnoreCase(value)) {
                return role;
            }
        }
        return null;
    }
}

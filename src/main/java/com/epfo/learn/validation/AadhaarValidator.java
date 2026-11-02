package com.epfo.learn.validation;

public final class AadhaarValidator {

    private AadhaarValidator() {
    }

    /** Returns null if the Aadhaar number is valid, otherwise a short reason. */
    public static String validate(String aadhaar) {
        if (aadhaar == null || !aadhaar.matches("[0-9]{12}")) {
            return "Aadhaar must be exactly 12 digits";
        }
        char first = aadhaar.charAt(0);
        if (first == '0' || first == '1') {
            return "Aadhaar must not start with 0 or 1";
        }
        if (!VerhoeffValidator.isValid(aadhaar)) {
            return "Aadhaar checksum is invalid";
        }
        return null;
    }
}
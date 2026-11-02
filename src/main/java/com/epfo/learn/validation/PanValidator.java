package com.epfo.learn.validation;

public final class PanValidator {

    private static final String HOLDER_TYPES = "PCHFATBLJG";

    private PanValidator() {
    }

    /** Returns null if the PAN is valid, otherwise a short reason. */
    public static String validate(String pan) {
        if (pan == null || !pan.matches("[A-Z]{5}[0-9]{4}[A-Z]")) {
            return "PAN format is invalid (expected AAAAA9999A)";
        }
        if (HOLDER_TYPES.indexOf(pan.charAt(3)) < 0) {
            return "PAN 4th letter must be one of P,C,H,F,A,T,B,L,J,G";
        }
        return null;
    }
}
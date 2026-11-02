package com.epfo.learn.validation;

public final class PassportValidator {

    private PassportValidator() {
    }

    /** Returns null if the passport number is valid, otherwise a short reason. */
    public static String validate(String passport) {
        if (passport == null || !passport.matches("[A-PR-WY][1-9][0-9]{6}")) {
            return "Passport format is invalid (expected a letter and 7 digits, e.g. N1234567)";
        }
        return null;
    }
}
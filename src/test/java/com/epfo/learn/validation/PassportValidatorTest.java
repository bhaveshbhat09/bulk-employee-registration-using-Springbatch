package com.epfo.learn.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class PassportValidatorTest {

    private static final String MSG =
            "Passport format is invalid (expected a letter and 7 digits, e.g. N1234567)";

    @Test
    void validPassportReturnsNull() {
        assertNull(PassportValidator.validate("N1234567"));
        assertNull(PassportValidator.validate("Y9876543"));
    }

    @Test
    void excludedFirstLettersAreRejected() {
        assertEquals(MSG, PassportValidator.validate("Q1234567"));
        assertEquals(MSG, PassportValidator.validate("X1234567"));
        assertEquals(MSG, PassportValidator.validate("Z1234567"));
    }

    @Test
    void zeroAfterTheLetterIsRejected() {
        assertEquals(MSG, PassportValidator.validate("N0234567"));
    }

    @Test
    void badFormatIsRejected() {
        assertEquals(MSG, PassportValidator.validate(null));
        assertEquals(MSG, PassportValidator.validate(""));
        assertEquals(MSG, PassportValidator.validate("n1234567"));
        assertEquals(MSG, PassportValidator.validate("N123456"));
        assertEquals(MSG, PassportValidator.validate("N12345678"));
    }
}
package com.epfo.learn.validation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class VerhoeffValidatorTest {

    @Test
    void shortKnownExampleIsValid() {
        assertTrue(VerhoeffValidator.isValid("2363"));
    }

    @Test
    void sampleAadhaarFromOurDesignIsValid() {
        assertTrue(VerhoeffValidator.isValid("234567890124"));
    }

    @Test
    void wrongLastDigitIsInvalid() {
        assertFalse(VerhoeffValidator.isValid("234567890123"));
        assertFalse(VerhoeffValidator.isValid("2368"));
    }

    @Test
    void nonDigitsAreInvalid() {
        assertFalse(VerhoeffValidator.isValid("23456789012A"));
        assertFalse(VerhoeffValidator.isValid("2345 6789 0124"));
    }

    @Test
    void nullAndEmptyAreInvalid() {
        assertFalse(VerhoeffValidator.isValid(null));
        assertFalse(VerhoeffValidator.isValid(""));
    }
}
package com.epfo.learn.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class AadhaarValidatorTest {

    @Test
    void validAadhaarReturnsNull() {
        assertNull(AadhaarValidator.validate("234567890124"));
    }

    @Test
    void nullOrEmptyIsRejected() {
        assertEquals("Aadhaar must be exactly 12 digits", AadhaarValidator.validate(null));
        assertEquals("Aadhaar must be exactly 12 digits", AadhaarValidator.validate(""));
    }

    @Test
    void wrongLengthIsRejected() {
        assertEquals("Aadhaar must be exactly 12 digits", AadhaarValidator.validate("23456789012"));
        assertEquals("Aadhaar must be exactly 12 digits", AadhaarValidator.validate("2345678901245"));
    }

    @Test
    void nonDigitsAreRejected() {
        assertEquals("Aadhaar must be exactly 12 digits", AadhaarValidator.validate("23456789012A"));
    }

    @Test
    void startingWithZeroOrOneIsRejected() {
        assertEquals("Aadhaar must not start with 0 or 1", AadhaarValidator.validate("034567890124"));
        assertEquals("Aadhaar must not start with 0 or 1", AadhaarValidator.validate("134567890124"));
    }

    @Test
    void wrongChecksumIsRejected() {
        assertEquals("Aadhaar checksum is invalid", AadhaarValidator.validate("234567890123"));
    }
}
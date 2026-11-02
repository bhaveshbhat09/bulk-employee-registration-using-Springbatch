package com.epfo.learn.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class PanValidatorTest {

    private static final String FORMAT_MSG = "PAN format is invalid (expected AAAAA9999A)";
    private static final String HOLDER_MSG = "PAN 4th letter must be one of P,C,H,F,A,T,B,L,J,G";

    @Test
    void validPanReturnsNull() {
        assertNull(PanValidator.validate("ABCPE1234F"));
    }

    @Test
    void allHolderTypesAreAccepted() {
        for (char holder : "PCHFATBLJG".toCharArray()) {
            assertNull(PanValidator.validate("ABC" + holder + "E1234F"));
        }
    }

    @Test
    void invalidHolderTypeIsRejected() {
        assertEquals(HOLDER_MSG, PanValidator.validate("ABCDE1234F"));
    }

    @Test
    void badFormatIsRejected() {
        assertEquals(FORMAT_MSG, PanValidator.validate(null));
        assertEquals(FORMAT_MSG, PanValidator.validate(""));
        assertEquals(FORMAT_MSG, PanValidator.validate("abcpe1234f"));
        assertEquals(FORMAT_MSG, PanValidator.validate("ABCPE123F"));
        assertEquals(FORMAT_MSG, PanValidator.validate("ABCPE12345"));
    }
}
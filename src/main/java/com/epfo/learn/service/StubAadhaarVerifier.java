package com.epfo.learn.service;

import org.springframework.stereotype.Component;

@Component
public class StubAadhaarVerifier implements AadhaarVerifier {

    @Override
    public String verify(String aadhaar) {
        // Simulation only: pretend UIDAI cannot find numbers ending in 7.
        if (aadhaar.endsWith("7")) {
            return "Aadhaar not found at UIDAI (simulated: numbers ending in 7 are rejected)";
        }
        return null;
    }
}
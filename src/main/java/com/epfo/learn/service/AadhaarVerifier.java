package com.epfo.learn.service;

public interface AadhaarVerifier {
    /** Returns null if the Aadhaar is accepted, otherwise the reason it was refused. */
    String verify(String aadhaar);
}
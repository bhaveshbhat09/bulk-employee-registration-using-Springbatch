package com.epfo.learn.batch;

import java.sql.Date;

public class StagingRecord {
    private final long stagingId;
    private final String fullName;
    private final String mobile;
    private final String aadhaar;
    private final String pan;
    private final String passport;
    private final Date dob;

    public StagingRecord(long stagingId, String fullName, String mobile,
                         String aadhaar, String pan, String passport, Date dob) {
        this.stagingId = stagingId;
        this.fullName = fullName;
        this.mobile = mobile;
        this.aadhaar = aadhaar;
        this.pan = pan;
        this.passport = passport;
        this.dob = dob;
    }
    public long getStagingId() { return stagingId; }
    public String getFullName() { return fullName; }
    public String getMobile() { return mobile; }
    public String getAadhaar() { return aadhaar; }
    public String getPan() { return pan; }
    public String getPassport() { return passport; }
    public Date getDob() { return dob; }
}
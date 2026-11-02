package com.epfo.learn.batch;

import java.sql.Date;

public class StagingRow {
    private final long batchId;
    private final int lineNo;
    private final String fullName;
    private final String mobile;
    private final String aadhaar;
    private final String pan;
    private final String passport;
    private final Date dob;

    public StagingRow(long batchId, int lineNo, String fullName, String mobile,
                      String aadhaar, String pan, String passport, Date dob) {
        this.batchId = batchId;
        this.lineNo = lineNo;
        this.fullName = fullName;
        this.mobile = mobile;
        this.aadhaar = aadhaar;
        this.pan = pan;
        this.passport = passport;
        this.dob = dob;
    }
    public long getBatchId() { return batchId; }
    public int getLineNo() { return lineNo; }
    public String getFullName() { return fullName; }
    public String getMobile() { return mobile; }
    public String getAadhaar() { return aadhaar; }
    public String getPan() { return pan; }
    public String getPassport() { return passport; }
    public Date getDob() { return dob; }
}
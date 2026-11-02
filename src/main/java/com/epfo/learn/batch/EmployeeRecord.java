package com.epfo.learn.batch;

public class EmployeeRecord {

    private String name;
    private String mobile;
    private String aadhaar;
    private String pan;
    private String passport;
    private String dob;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public String getAadhaar() { return aadhaar; }
    public void setAadhaar(String aadhaar) { this.aadhaar = aadhaar; }
    public String getPan() { return pan; }
    public void setPan(String pan) { this.pan = pan; }
    public String getPassport() { return passport; }
    public void setPassport(String passport) { this.passport = passport; }
    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    @Override
    public String toString() {
        return "EmployeeRecord[name=" + name + ", mobile=" + mobile + ", aadhaar=" + aadhaar
                + ", pan=" + pan + ", passport=" + passport + ", dob=" + dob + "]";
    }
}
package com.example.receivecrime.Models;

public class ReportHistoryModel {
    String USERID, CRIME, DESCRIPTION, LOCATION, STATUS;

    public ReportHistoryModel() {
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getCRIME() {
        return CRIME;
    }

    public void setCRIME(String CRIME) {
        this.CRIME = CRIME;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public String getLOCATION() {
        return LOCATION;
    }

    public void setLOCATION(String LOCATION) {
        this.LOCATION = LOCATION;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }
}


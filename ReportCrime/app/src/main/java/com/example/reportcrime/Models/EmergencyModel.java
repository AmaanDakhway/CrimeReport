package com.example.reportcrime.Models;

public class EmergencyModel {
    String USERID, LOCATION, EMERGENCY_STATUS;

    public EmergencyModel() {
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getLOCATION() {
        return LOCATION;
    }

    public void setLOCATION(String LOCATION) {
        this.LOCATION = LOCATION;
    }

    public String getEMERGENCY_STATUS() {
        return EMERGENCY_STATUS;
    }

    public void setEMERGENCY_STATUS(String EMERGENCY_STATUS) {
        this.EMERGENCY_STATUS = EMERGENCY_STATUS;
    }
}


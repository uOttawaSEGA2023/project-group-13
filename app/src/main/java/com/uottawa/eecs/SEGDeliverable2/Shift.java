package com.uottawa.eecs.SEGDeliverable2;

// Shift.java
public class Shift {
    private String emNum;
    private String date;
    private String startTime;
    private String endTime;

    public Shift() {
        // Default constructor required for Firebase
    }

    public Shift(String emNum, String date, String startTime, String endTime) {
        this.emNum = emNum;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;

    }

    public String getEmNum() {
        return emNum;
    }

    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}





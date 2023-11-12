package com.uottawa.eecs.SEGDeliverable2;

// Shift.java
public class Shift {
    private String userEmail;
    private String date;
    private String startTime;
    private String endTime;

    // Default constructor required for Firebase
    public Shift() {
    }

    public Shift(String userEmail, String date, String startTime, String endTime) {
        this.userEmail = userEmail;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getter and setter for userEmail
    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
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





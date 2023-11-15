package com.uottawa.eecs.SEGDeliverable3;

// Shift.java
public class Shift {
    private String userEmail;
    private String date;
    private String startTime;
    private String endTime;
    private String firebaseKey;

    private Doctor doctor;

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

    public String getKey() {
        return firebaseKey;
    }

    public void setKey(String firebaseKey) {
        this.firebaseKey = firebaseKey;
    }



}







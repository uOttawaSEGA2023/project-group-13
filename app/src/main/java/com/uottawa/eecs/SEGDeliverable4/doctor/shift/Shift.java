package com.uottawa.eecs.SEGDeliverable4.doctor.shift;

import com.google.firebase.database.PropertyName;
import com.uottawa.eecs.SEGDeliverable4.doctor.Doctor;
import com.uottawa.eecs.SEGDeliverable4.patient.Appointment;

import java.util.ArrayList;

// Shift.java
public class Shift {
    private String userEmail;
    private String date;
    private String startTime;
    private String endTime;
    private String firebaseKey;

    private Doctor doctor;
    private ArrayList<Appointment> timeslots;

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

    public ArrayList<Appointment> getTimeslots() {
        return timeslots;
    }

    @PropertyName("timeslots")
    public void setTimeslots(ArrayList<Appointment> timeslots) {
        this.timeslots = timeslots;
    }


    public void addTimeslots(Appointment appt){
        this.timeslots.add(appt);
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }


}







package com.uottawa.eecs.SEGDeliverable2;

import java.time.LocalDate;
import java.time.LocalTime;
public class Appointment {

    // Class variables
    private Patient patient;
    private Shift shift;
    private LocalTime startTime;
    private LocalTime endTime;
    private int status;

    // Constructors
    public Appointment(Patient patient, Shift shift, LocalTime startTime, int status) {
        this.patient = patient;
        this.shift = shift;
        this.startTime = startTime;
        this.endTime = startTime.plusMinutes(30);
        this.status = 0;
    }

    // Setters
    public void setPatient(Patient patient) { this.patient = patient; }

    public void setShift(Shift shift) { this.shift = shift; }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
        this.endTime = startTime.plusMinutes(30);
    }

    public void setStatus(int status) { this.status = status; }

    // Accessors
    public Patient getPatient() { return patient; }

    public Shift getShift() { return shift; }

    public LocalTime getStartTime() { return startTime; }

    public LocalTime getEndTime() { return endTime; }

    public int getStatus() { return status; }

    public Doctor getDoctor() { return shift.getDoctor(); }
}

package com.uottawa.eecs.SEGDeliverable2;

import static java.time.temporal.ChronoUnit.MINUTES;

import java.time.LocalDate;
import java.time.LocalTime;

public class Shift {

    // Class variables
    private Doctor doctor;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Appointment[] appts;

    // Constructors
    public Shift(Doctor doctor, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.doctor = doctor;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        // used an array for now (Need to consider: pending appts could overlap with each other, must still be associated with the correct shift.
        // so would need an ArrayList or only access it thru Firebase
        this.appts = new Appointment[(int)startTime.until(endTime, MINUTES)/30];
    }

    // Setters
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public void setDate(LocalDate date) { this.date = date; }

    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    // Accessors
    public Doctor getDoctor() { return doctor; }

    public LocalDate getDate() { return date; }

    public LocalTime getStartTime() { return startTime; }

    public LocalTime getEndTime() { return endTime; }

    public Appointment[] getAppts() { return appts; }
}

package com.uottawa.eecs.SEGDeliverable3;

public class Appointment {

    // Class variables
    private String email, drEmail;
//    private String shiftKey;
    private String startTime;
    private String endTime;
    private int status;
    private String date;

   // public Patient patient;

    // we need the info directly for firebase

    // Constructors
    // times have to be strings or firebase gets angry
    public Appointment(String email, String startTime, String endTime, int status, String date, String drEmail) {
        this.email = email;
       // this.shiftKey = shift;
        // TO BE CHANGED TO USE SHIFTS FOR THE DATE VVV (aka use shift key to get it - but we need the appointments to be attached to shifts first!)
        this.date = date;
        this.startTime = startTime; // also i dont check that these are 30 mins apart might want to lol
        // but also its safe to assume they are since we'd be checking that when we make the appointment
        this.endTime = endTime;
        this.status = status; // -1 = rejected, 0 = pending approval from doc, 1 = approved from doc
        this.drEmail = drEmail;

    }
    public Appointment(){

    }

    public Appointment(String email, String startTime, String endTime, String date, String drEmail) {
        this.email = email;
        // this.shiftKey = shift;
        // TO BE CHANGED TO USE SHIFTS FOR THE DATE VVV (aka use shift key to get it - but we need the appointments to be attached to shifts first!)
        this.date = date;
        this.startTime = startTime; // also i dont check that these are 30 mins apart might want to lol
        // but also its safe to assume they are since we'd be checking that when we make the appointment
        this.endTime = endTime;
        this.status = 0; // -1 = rejected, 0 = pending approval from doc, 1 = approved from doc
        this.drEmail = drEmail;

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

   // public String getShiftKey() {return shiftKey;}

   // public void setShiftKey(String shiftKey) {this.shiftKey = shiftKey;}

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

   // public void setPatient(Patient p) {this.patient = p;}

   // public Patient getPatient() {return patient;}

    public String getDrEmail() {
        return drEmail;
    }

    public void setDrEmail(String drEmail) {
        this.drEmail = drEmail;
    }

    public String sanitizeEmail(String email) {
        if (email != null) {
            return email.replaceAll("[^a-zA-Z0-9]", "_")
                    .toLowerCase();
        } else {
            // in case of error
            return "user email not showing";
        }
    }
}

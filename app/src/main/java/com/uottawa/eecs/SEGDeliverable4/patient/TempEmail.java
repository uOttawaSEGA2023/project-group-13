package com.uottawa.eecs.SEGDeliverable4.patient;

public class TempEmail {
    // only serves to read an email from firebase easily
    String email;
    public TempEmail(){
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TempEmail(String email){
        this.email = email;
    }




}

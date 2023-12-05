package com.uottawa.eecs.SEGDeliverable4.patient;

import com.uottawa.eecs.SEGDeliverable4.main.Address;
import com.uottawa.eecs.SEGDeliverable4.main.Person;

//class for patient
public class Patient extends Person {
    private String healthCardNumber;


    //constructor 
    public Patient(String firstName, String lastName, String email, String password, String phone, Address address, String healthCardNumber, String type) {
        super(firstName, lastName, email, password, phone, address, type);
        this.healthCardNumber = healthCardNumber;

    }

    public String getHealthCardNumber() {
        return healthCardNumber;
    }

    public void setHealthCardNumber(String healthCardNumber) {this.healthCardNumber = healthCardNumber;}



}
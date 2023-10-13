package com.uottawa.eecs.registrationform;

public class Patient extends Person  {
    private String healthCardNumber;
    public Patient(String firstName, String lastName, String email, String password, String phone, Address address, String healthCardNumber) {
        super(firstName, lastName, email, password, phone, address);
        this.healthCardNumber = healthCardNumber;
    }

    public String getHealthCardNumber() {
        return healthCardNumber;
    }

    public void setHealthCardNumber(String healthCardNumber) {
        this.healthCardNumber = healthCardNumber;
    }

}

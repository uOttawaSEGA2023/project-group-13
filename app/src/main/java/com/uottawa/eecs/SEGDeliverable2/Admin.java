package com.uottawa.eecs.SEGDeliverable2;

//class to hold the admin
public class Admin extends Person{
    public Admin(String username, String password) {
        super(username, password, "Admin");
        setRegistrationStatus(1);
    }

}

package com.uottawa.eecs.SEGDeliverable4.admin;

import com.uottawa.eecs.SEGDeliverable4.main.Person;

//class to hold the admin
public class Admin extends Person {
    public Admin(String username, String password) {
        super(username, password, "Admin");
        setRegistrationStatus(1);
    }

}

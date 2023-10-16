package com.uottawa.eecs.SEGDeliverable1;

import java.util.ArrayList;

// used to manage users 
public final class Users {
    public static ArrayList<Person> users;

    //constructors 
    public Users () {

    }

    public static ArrayList<Person> getUsers() {
        return users;
    }

    public static void updateArrList(Person p) {
        users.add(p);
    }

    public static void instantiate() {
        if(users == null || users.isEmpty()) {
            users = new ArrayList<>();
        }
    }

}

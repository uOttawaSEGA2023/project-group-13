package com.uottawa.eecs.SEGDeliverable2;

// person class (parent of patient/doctor)
public class Person {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private Address address;

    private String type;

    private int registrationStatus;

    public Person(String firstName, String lastName, String email, String password, String phone, String type) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = new Address();

    }
    public Person(String firstName, String lastName, String email, String password, String phone, Address address, String type) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.type = type;
    }

    public Person(String type) {
        this.address = new Address();
        this.type = type;
    }

    public Person(String username, String password, String type) {
        this.email = username;
        this.password = password;
        this.type = type;

    }

    /*
     * Setter Methods
     */
    public void setfName(String fName) {
        this.firstName = fName;
    }

    public void setlName(String lName) {
        this.lastName = lName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setpWord(String pWord) {
        this.password = pWord;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setAddress(String streetNum, String street, String city, String ProviTerri) {
        this.address = new Address(streetNum, street, city, ProviTerri);
    }

    public void setpNum(String pNum) {
        this.phone = pNum;
    }

    public void setRegistrationStatus(int status) {this.registrationStatus = status;}

    /*
     * Getter Methods
     */

    public Address getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getfName() {
        return firstName;
    }

    public String getlName() {
        return lastName;
    }

    public String getpWord() {
        return password;
    }

    public String getpNum() {
        return phone;
    }

    public String getType() { // only get since don't need to change types
        return type;
    }

    public int getRegistrationStatus() {return this.registrationStatus;}

}
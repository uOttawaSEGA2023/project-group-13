package com.uottawa.eecs.registrationform;

public class Person {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phone;
    private Address address;

    public Person(String firstName, String lastName, String email, String password, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = new Address();
    }
    public Person(String firstName, String lastName, String email, String password, String phone, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
    }


    public Person() {

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
}

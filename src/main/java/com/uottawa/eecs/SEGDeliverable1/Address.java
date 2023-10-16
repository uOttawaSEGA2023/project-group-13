package com.uottawa.eecs.SEGDeliverable1;

//address class for person 
public class Address {
    private String street, provTerri, city, streetNum;

    public Address(String streetNum, String street, String city, String provTerri) {
        this.streetNum = streetNum;
        this.street = street;
        this.city = city;
        this.provTerri = provTerri;

    }

    public Address() {
        // we can create a blank one, but it will need to be filled
        // this way the doctor can have an address attribute, but we will know it's not instantiated since attributes are null, etc.
        this.streetNum = null;
        this.street = null;
        this.city = null;
        this.provTerri = null;
    }

    /*
     * Setter Methods
     */

    public void setProvTerri(String provTerri) {
        this.provTerri = provTerri;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setStreetNum(String streetNum) {
        this.streetNum = streetNum;
    }

    public void setCity(String city) {
        this.city = city;
    }

    /*
     * Getter Methods
     */

    public String getStreetNum() {
        return streetNum;
    }

    public String getProvTerri() {
        return provTerri;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }
}
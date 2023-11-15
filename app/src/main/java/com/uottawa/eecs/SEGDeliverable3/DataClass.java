package com.uottawa.eecs.SEGDeliverable3;

public class DataClass {
    // aspects directly from person class
    private String firstName, lastName, email, password, phone, type, employeeOrHealthCardNumber, specialties;

    // aspects from address class
    private String streetNumber, street, city, province;

    // registration status
    private int registrationStatus;




    // default constructor
    DataClass() {

    }

    //actual constructor
    public DataClass(Person profileToStore) {
        // move everything over into this object, this object is what will be sent to firebase
        this.firstName = profileToStore.getfName();
        this.lastName = profileToStore.getlName();
        this.email = profileToStore.getEmail();
        this.password = profileToStore.getpWord();
        this.phone = profileToStore.getpNum();
        this.type = profileToStore.getType();

        this.streetNumber = profileToStore.getAddress().getStreetNum();
        this.street = profileToStore.getAddress().getStreet();
        this.city = profileToStore.getAddress().getCity();
        this.province = profileToStore.getAddress().getProvTerri();

        this.registrationStatus = profileToStore.getRegistrationStatus();

        // need to see what kind of person they are...
        // we can't just use person class as it causes an error when trying to display, we need this to be able to display the health/employee number and specialties
        if(profileToStore instanceof Doctor) {
            Doctor doctorProfile = (Doctor) profileToStore;
            this.employeeOrHealthCardNumber = doctorProfile.getEmNum();
            this.specialties = doctorProfile.getSpecialty().toString().substring(1,doctorProfile.getSpecialty().toString().length()-1);
        }
        else if(profileToStore instanceof  Patient) {
            Patient patientProfile = (Patient) profileToStore;
            this.employeeOrHealthCardNumber = patientProfile.getHealthCardNumber();
            this.specialties = "";
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmployeeOrHealthCardNumber() {
        return employeeOrHealthCardNumber;
    }

    public void setEmployeeOrHealthCardNumber(String employeeOrHealthCardNumber) {
        this.employeeOrHealthCardNumber = employeeOrHealthCardNumber;
    }


    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(int registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public String getSpecialties() {
        return specialties;
    }

    public void setSpecialties(String specialties) {
        this.specialties = specialties;
    }
}

package com.example.login;

public class Testdata {


    String email, password, occupation, name;

    public String getName(){return name;}
    public void  setName( String name){
        this.name = name;
    }
    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
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



    public Testdata() {
    }

    public Testdata(String email, String password, String occupation, String name){
        this.email = email;
        this.password = password;
        this.occupation = occupation;
        this.name = name;
    }

    public Testdata[] Createdata(){
        Testdata Maryse = new Testdata("maryse0423@gmail.com", "Seg2105", "patient", "Maryse");
        Testdata Olive = new Testdata("olive.soki@uottawa.ca", "Soki23", "doctor", "Olive");
        Testdata Haden = new Testdata("Haden2004@gmail.com", "Hulk", "patient", "Haden");

        Testdata[] users = {Maryse, Olive, Haden};

        return  users;
    }

}

package com.uottawa.eecs.registrationform;

import java.util.ArrayList;

public class Doctor extends Person{
    /*
     *  Class variables
     */
    private String  emNum;

    private ArrayList<String> specialty;

    public Doctor(String fName, String lName, String email, String pWord, String pNum, String emNum, ArrayList<String> specialty) {
        super(fName, lName, email, pWord, pNum);
        this.emNum = emNum;
        this.specialty = specialty;

    }

    public Doctor() {
        this.specialty = new ArrayList<String>();
    }

    /*
     * Setter Methods
     */

    public void setEmNum(String emNum) {
        this.emNum = emNum;
    }

    public void addSpecialty(String newSpecialty) {
        specialty.add(newSpecialty);
    }

    public void removeSpecialty(String oldSpecialty) {
        specialty.remove(oldSpecialty);
    }

    public void clearSpecialties() {
        specialty.clear();
    }

    /*
     * Getter Methods
     */
    public String getEmNum() {
        return emNum;
    }

    public ArrayList<String> getSpecialty() {
        return specialty;
    }

}
package com.uottawa.eecs.SEGDeliverable2;
// import to store specialties   
import java.util.ArrayList;
 
public class Doctor extends Person {
    /*
     *  Class variables
     */
    private String emNum;

    private ArrayList<String> specialty;
    private ArrayList<Shift> shifts;



    //constructors
    public Doctor(String fName, String lName, String email, String pWord, String pNum, String emNum, ArrayList<String> specialty, String type) {
        super(fName, lName, email, pWord, pNum, type);
        this.emNum = emNum;
        this.specialty = specialty;
        this.shifts = new ArrayList<>();


    }

    public Doctor(String type) {
        super(type);
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

    public ArrayList<String> getSpecialty() {return specialty;}
    public ArrayList<Shift> getShifts() {
        return shifts;
    }
}
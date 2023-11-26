package com.uottawa.eecs.SEGDeliverable3.doctor;
// import to store specialties
import com.uottawa.eecs.SEGDeliverable3.main.Person;
import com.uottawa.eecs.SEGDeliverable3.doctor.shift.Shift;

import java.util.ArrayList;
import java.util.List;

public class Doctor extends Person {
    /*
     *  Class variables
     */
    private String emNum;

    private ArrayList<String> specialty;



    private ArrayList<Float> listOfRatings = new ArrayList<>();
    private float rating = 0;


    private ArrayList<Shift> shifts; // upcoming shifts


    //constructors
    public Doctor(String fName, String lName, String email, String pWord, String pNum, String emNum, ArrayList<String> specialty, String type, float rating) {
        super(fName, lName, email, pWord, pNum, type);
        this.emNum = emNum;
        this.specialty = specialty;
        this.shifts = new ArrayList<>();
        this.rating = rating;

    }

    public Doctor(String type) {
        super(type);
        this.specialty = new ArrayList<>();

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

    public void addShift(Shift newShift) { shifts.add(newShift); }

    public void removeShift(Shift oldShift) { shifts.remove(oldShift); }



    public void addRating(float num){
        this.listOfRatings.add(num);
    }

    public ArrayList <Float> getAllRatings(){
        return this.listOfRatings;
    }



    public void setRating(float r){
        rating = 0;
        this.addRating(r);
        for(float num: this.getAllRatings()){
            rating += num;
        }
       this.rating = this.rating/this.getAllRatings().size();
    }



    /*
     * Getter Methods
     */
    public String getEmNum() {
        return emNum;
    }

    public ArrayList<String> getSpecialty() {return specialty;}

    public ArrayList<Shift> getShifts() { return shifts; }

    public float getRating(){

        return this.rating;
    }

}
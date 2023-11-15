package com.uottawa.eecs.SEGDeliverable3;

// class to store shifts to be displayed from firebase with the adapter
public class UpcomingShiftModel {
    String Date;
    String Time;

    UpcomingShiftModel(String date, String time){
        this.Date = date;
        this.Time = time;
    }



    public String getDate(){
        return Date;
    }

    public String getTime(){
        return Time;
    }

    public void setDate(String date){
        this.Date = date;
    }


    public void setTime(String time){
        this.Time = time;
    }
}




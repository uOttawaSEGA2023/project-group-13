package com.example.upcomingshifts;

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




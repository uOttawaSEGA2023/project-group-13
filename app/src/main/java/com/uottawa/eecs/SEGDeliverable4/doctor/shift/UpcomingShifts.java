package com.uottawa.eecs.SEGDeliverable4.doctor.shift;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.uottawa.eecs.SEGDeliverable4.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class UpcomingShifts extends AppCompatActivity {

    /*
    Need to add firebase. Check for 30 minute intervals + that the date hasn't passed.
    And add a delete shift button to the cardview.
     */


    //Stores upcoming shift data

    ArrayList<UpcomingShiftModel> UpcomingShiftModels = new ArrayList<>();

    //adapter for RecyclerView
    private UpcomingShiftsAdapter adapter;

    //Stores shift and time details
    private String newShift;
    private String newTime;
    String displayTime;

    //Store shift and time strings
    public ArrayList<String>  Shifts = new ArrayList<>();
    public ArrayList<String> Time = new ArrayList<>();

    //to set the current date and time
    Calendar currentCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upcomingshifts);


        //Find recyclerView in layout
        RecyclerView recyclerView = findViewById(R.id.UpcomingShift);
        //check if shifts and time are not null before setting up the models
        //with firebase it would check if the doctor has any shifts already.
        if (Shifts !=null && Time !=null) {
            setUpcomingShiftModels();
        }

        //initialize adapter
        adapter = new UpcomingShiftsAdapter(this, UpcomingShiftModels);


        recyclerView.setAdapter(adapter);
        Button addShift = findViewById(R.id.addShift);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        addShift.setOnClickListener( new View.OnClickListener(){
            public void onClick(View view){
                //call methods to get shift and time details
                endTime();
                startTime();
                openCalendar();
                UpcomingShiftModel addedShift = new UpcomingShiftModel(newShift, newTime);
                int positionToAdd = Shifts.size();

                adapter.addItem(addedShift,positionToAdd);
                setUpcomingShiftModels();

            }
        });
    }

    //sets up upcoming shift models from shift and time list
    private void setUpcomingShiftModels(){
        ArrayList<String> shiftDate =  Shifts;
        ArrayList<String> shiftTime =  Time;

        UpcomingShiftModels = new ArrayList<>();

        if(shiftDate.size() == shiftTime.size()) {
            for (int i = 0; i < shiftDate.size(); i++) {
                UpcomingShiftModels.add(new UpcomingShiftModel(shiftDate.get(i), shiftTime.get(i)));

            }
        }



    }

    private void openCalendar(){
        // Create a DatePickerDialog with the current date
        DatePickerDialog dialog = new DatePickerDialog(this, (datePicker, year, month, day) -> {

            // Format the selected date
            String displayShift = String.format(Locale.getDefault(),"%02d/%02d/%d", day,month +1, year);

            newShift = ( "Date: " + displayShift);
            // Add the formatted date to the Shifts list. Should be removed later
            Shifts.add(newShift);
        },
                currentCalendar.get(Calendar.YEAR),
                currentCalendar.get(Calendar.MONTH),
                currentCalendar.get(Calendar.DAY_OF_MONTH)

        );

        //show the date picker dialog
        dialog.show();

    }

    private void startTime() {
        //Create a TimePickerDialog with the current time
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Format the selected time
                displayTime = String.format(Locale.getDefault(),"%02d:%02d", hourOfDay,minute);
            }
        },
                currentCalendar.get(Calendar.HOUR_OF_DAY),
                currentCalendar.get(Calendar.MINUTE),
                true
        );

        // Show the time picker dialog
        dialog.show();
    }


    private void endTime() {
        //Create a TimePickerDialog with the current tim
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // Format the selected time
                String endTime = String.format(Locale.getDefault(),"%02d:%02d", hourOfDay,minute);

                newTime = ("Time:" + displayTime + "-" + endTime);

                Time.add(newTime);

                // add the shift with its time
                UpcomingShiftModel addedShift = new UpcomingShiftModel(newShift, newTime);
                int positionToAdd = Shifts.size();

                // display it locally
                adapter.addItem(addedShift,positionToAdd);
                setUpcomingShiftModels();



            }
        },
                currentCalendar.get(Calendar.HOUR_OF_DAY),
                currentCalendar.get(Calendar.MINUTE),
                true);
        dialog.show();
    }

}


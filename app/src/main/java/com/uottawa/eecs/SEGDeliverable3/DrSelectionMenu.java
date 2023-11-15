package com.uottawa.eecs.SEGDeliverable3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class DrSelectionMenu extends AppCompatActivity {
    Button shifts, appointments;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_selection_menu);

        shifts = findViewById(R.id.shifts);
        appointments = findViewById(R.id.appointment);

        shifts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenDoctorShift();
            }
        });

        appointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAppointmentMenu();
            }
        });
    }

    private void openAppointmentMenu() {
        // get information from the previous intent, and pass it along to the next activity
        Intent oldIntent = getIntent();
        String userEmail = oldIntent.getStringExtra("UserEmail");
        Intent intent= new Intent(this, DoctorApptHomePage.class);
        intent.putExtra("UserEmail", userEmail);  // Include userEmail in the Intent
        Log.e("userEmail: ", userEmail);

        startActivity(intent);
    }

    public void OpenDoctorShift(){
        // get information from the previous intent, and pass it along to the next activity
        Intent oldIntent = getIntent();
        String userEmail = oldIntent.getStringExtra("UserEmail");

        Intent intent = new Intent(this, DoctorShiftsActivity.class);
        intent.putExtra("UserEmail", userEmail);  // Include userEmail in the Intent
        Log.e("userEmail: ", userEmail);

        startActivity(intent);
    }

}
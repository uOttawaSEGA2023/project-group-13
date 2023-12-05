package com.uottawa.eecs.SEGDeliverable4.main.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.uottawa.eecs.SEGDeliverable4.R;

// menu to pick what form to fill out 
public class RegisterMenu extends AppCompatActivity {

    private Button button, pbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_menu);

        button = (Button) findViewById(R.id.DoctorBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDoctorMenu();
            }
        });

        pbutton = (Button) findViewById(R.id.PatientBtn);
        pbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPatientMenu();
            }
        });
    }

    public void openDoctorMenu(){

        Intent intent= new Intent(this, RegisterDoctor.class);
        startActivity(intent);
    }

    public void openPatientMenu(){

        Intent intent= new Intent(this, RegisterPatient.class);
        startActivity(intent);
    }
}
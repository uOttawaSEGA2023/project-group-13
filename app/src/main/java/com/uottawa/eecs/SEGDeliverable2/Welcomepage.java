package com.uottawa.eecs.SEGDeliverable2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

// welcome page which is shown after login
public class Welcomepage extends AppCompatActivity {

    Button continuebutton;
    String username;
    String userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcomepage);

        TextView textView = (TextView) findViewById(R.id.Occupation);
        Bundle userTypes = getIntent().getExtras();

        if(userTypes != null){
            String currentOccupation = userTypes.getString("Occupation");
            textView.setText("Welcome! You are logged in as a " + currentOccupation);

        }
        Intent intent = getIntent();
        if (intent != null) {
            userEmail = intent.getStringExtra("UserEmail");
            String occupation = intent.getStringExtra("Occupation");}

        continuebutton = findViewById(R.id.continueButton);

        continuebutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(userTypes.getString("Occupation").equals("Admin")) {
                    OpenAdminHomePage();
                } else if (userTypes.getString("Occupation").equals("Doctor")) {
                    OpenDoctorShift();


                }
                else {
                    OpenLogoutPage();
                }
            }
     });
    }

    public void OpenLogoutPage(){
        Intent intent = new Intent(this, LogoutPage.class);
        startActivity(intent);
    }

    public void OpenAdminHomePage(){
        Intent intent = new Intent(this, AdminHomePage.class);
        startActivity(intent);
    }


    public void OpenDoctorShift(){
        Intent intent = new Intent(this, DoctorShiftsActivity.class);
        intent.putExtra("UserEmail", userEmail);  // Include userEmail in the Intent
        intent.putExtra("SanitizedUsername", username);
        startActivity(intent);

    }

}
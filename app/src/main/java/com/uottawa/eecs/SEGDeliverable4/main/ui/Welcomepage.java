package com.uottawa.eecs.SEGDeliverable4.main.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.uottawa.eecs.SEGDeliverable4.R;
import com.uottawa.eecs.SEGDeliverable4.admin.utility.AdminHomePage;
import com.uottawa.eecs.SEGDeliverable4.doctor.DrSelectionMenu;
import com.uottawa.eecs.SEGDeliverable4.patient.PatientHomePage;

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
            String occupation = intent.getStringExtra("Occupation");
        }


        continuebutton = findViewById(R.id.continueButton);


        continuebutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(userTypes.getString("Occupation").equals("Admin")) {
                    OpenAdminHomePage();
                } else if (userTypes.getString("Occupation").equals("Doctor")) {
                    openDrSelectionMenu();
                } else {
                    OpenPatientMenu();
                }
            }
     });
    }

    public void OpenPatientMenu() {
        Intent intent = new Intent(this, PatientHomePage.class);
        intent.putExtra("UserEmail", userEmail);
        Log.e("PATIENT EMAIL IN WELCOMEPAGE: ", userEmail);
        startActivity(intent);
    }

    public void OpenAdminHomePage(){
        Intent intent = new Intent(this, AdminHomePage.class);
        startActivity(intent);
    }
    public void openDrSelectionMenu(){
        Intent intent = new Intent(this, DrSelectionMenu.class);
        intent.putExtra("UserEmail", userEmail);
        //intent.putExtra("SanitizedUsername", username);
        startActivity(intent);
    }

}
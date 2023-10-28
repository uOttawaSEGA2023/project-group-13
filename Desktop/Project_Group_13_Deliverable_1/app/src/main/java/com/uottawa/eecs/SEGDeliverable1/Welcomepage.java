package com.uottawa.eecs.SEGDeliverable1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

// welcome page which is shown after login
public class Welcomepage extends AppCompatActivity {


    Button continuebutton;
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

        continuebutton = findViewById(R.id.continueButton);

        continuebutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                OpenLogoutPage();
            }
     });
    }

    public void OpenLogoutPage(){
        Intent intent = new Intent(this, LogoutPage.class);
        startActivity(intent);
    }
}
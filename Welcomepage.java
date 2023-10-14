package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Welcomepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcomepage);

        TextView textView = (TextView) findViewById(R.id.Occupation);
        Bundle userTypes = getIntent().getExtras();
        if(userTypes != null){
            String currentOccupation = userTypes.getString("Occupation");
            textView.setText("Welcome! You are logged in as a" + currentOccupation);
        }
    }
}
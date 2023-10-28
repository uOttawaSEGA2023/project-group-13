package com.uottawa.eecs.SEGDeliverable2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

// main homescreen, which is simply a logout button as of now 
public class LogoutPage extends AppCompatActivity {
    private Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout_page);

        buttonLogout = (Button) findViewById(R.id.LogoutButton);

        //function for when logout button is clicked 
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToHomePage();
            }
        });

    }

    public void returnToHomePage(){

        Intent intent= new Intent(this,MainMenu.class);
        startActivity(intent);
    }
}
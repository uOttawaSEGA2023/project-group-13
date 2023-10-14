package com.example.hams2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LogoutPage extends AppCompatActivity {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout_page);

        button = (Button) findViewById(R.id.LogoutButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToHomePage();
            }
        });
    }

    public void returnToHomePage(){

        Intent intent= new Intent(this,HomeMenu.class);
        startActivity(intent);
    }
}
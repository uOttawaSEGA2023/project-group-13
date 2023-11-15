package com.uottawa.eecs.SEGDeliverable3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        Button button = (Button) findViewById(R.id.Signup);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegisterMenu();
            }
        });

       Button buttonLogin = (Button) findViewById(R.id.Login);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLoginMenu();
            }
        });
    }

    public void openRegisterMenu(){

        Intent intent= new Intent(this, RegisterMenu.class);
        startActivity(intent);
    }

    public void openLoginMenu(){

        Intent intent= new Intent(this, LoginPage.class);
        startActivity(intent);
    }
}
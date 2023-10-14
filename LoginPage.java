package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class LoginPage extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;
    EditText username;
    EditText password;
    MaterialButton loginbutton;

    String currentOccupation;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

       // reference = FirebaseDatabase.getInstance().getReference("users");
       // String name = "Maryse";

        //Testdata Maryse = new Testdata("maryse0423@gmail.com", "Seg2105", "doctor", "Maryse");
        //Testdata Olive = new Testdata("olive.soki@gmail.com", "Soki23", "patient", "Olive");

        //reference.child(name).setValue(Maryse);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginbutton = (MaterialButton) findViewById(R.id.loginbutton);


        Testdata Maryse = new Testdata("maryse0423@gmail.com", "Seg2105", "patient", "Maryse");
        Testdata Olive = new Testdata("olive.soki@uottawa.ca", "Soki23", "doctor", "Olive");
        Testdata Haden = new Testdata("Haden2004@gmail.com", "Hulk", "patient", "Haden");

        Testdata[] users = {Maryse, Olive, Haden};




        loginbutton.setOnClickListener(new View.OnClickListener(){
            @Override
                    public void onClick(View v){


                String loginUsername = username.getText().toString();
                String loginPassword = password.getText().toString();

                Checkusername(loginUsername);
                CheckPassword(loginPassword);

                        for(int i = 0; i < users.length; i++){
                            if (Objects.equals(users[i].getEmail(), loginUsername)){
                                if(Objects.equals((users[i].getPassword()),loginPassword)){
                                    currentOccupation = users[i].getOccupation();
                                    OpenWelcomePage();
                                    break;
                                }
                                else{
                                    Toast.makeText(LoginPage.this, "LOGIN FAILED", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }



            }
        });



    }

    public void Checkusername(String email){
        if(email.equals(null)){
            Toast.makeText(LoginPage.this, "username cannot be empty", Toast.LENGTH_LONG).show();
        }
        else {
            if (!(email.contains("@"))) {
                Toast.makeText(LoginPage.this, "username must have an @", Toast.LENGTH_LONG).show();
            } else if (!((email.contains(".com")) || (email.contains(".ca")))){
                Toast.makeText(LoginPage.this, "username must be an email", Toast.LENGTH_LONG).show();

            }
        }

    }

    public void CheckPassword(String password){
        if(password == null){
            Toast.makeText(LoginPage.this, "password cannot be empty", Toast.LENGTH_LONG).show();
        }
    }

    public void OpenWelcomePage(){
        Intent intent = new Intent(this, Welcomepage.class);
        //passing username data to the next activity
        intent.putExtra("Occupation", currentOccupation);
        startActivity(intent);
    }

}



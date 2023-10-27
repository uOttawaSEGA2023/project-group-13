package com.uottawa.eecs.SEGDeliverable1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;
import java.util.regex.*;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class LoginPage extends AppCompatActivity {

    //FirebaseDatabase database;
    //DatabaseReference reference;
    EditText username;
    EditText password;
    MaterialButton loginbutton;

    String currentOccupation;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // reference = FirebaseDatabase.getInstance().getReference("users");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginbutton = (MaterialButton) findViewById(R.id.loginbutton);



        //what happens when to click login button (authenticate)
        loginbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
            boolean found = false;

                String loginUsername = username.getText().toString();
                String loginPassword = password.getText().toString();

                try {
                    Checkusername(loginUsername);
                    CheckPassword(loginPassword);
                } catch(Exception e) {
                    Toast.makeText(LoginPage.this, "ERROR: " +e.getMessage() , Toast.LENGTH_LONG).show();
                }

                String sanitizedUsername = loginUsername
                        .replaceAll("[^a-zA-Z0-9]", "_")
                        .toLowerCase();



                //creates a reference to the database
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                //Query checkUserDatabase = reference.orderByChild("email").equalTo(sanitizedUsername);

                //checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener(){

                //checks if there is a child with the email
                reference.child(sanitizedUsername).addListenerForSingleValueEvent(new ValueEventListener(){
                @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot){

                        if(snapshot.exists()){
                            username.setError(null);
                           String passwordFromDB = snapshot.child("pWord").getValue(String.class);

                           if(Objects.equals(passwordFromDB, loginPassword)){
                               username.setError(null);
                               String type = snapshot.child("type").getValue(String.class);
                               int status = snapshot.child("status").getValue(Integer.class);
                               if(status == 0){
                                   Toast.makeText(LoginPage.this, "Unable to login since your request has not been processed", Toast.LENGTH_LONG).show();
                               }
                               else if(status == -1){
                                   Toast.makeText(LoginPage.this, "Unable to login since your request has been rejected", Toast.LENGTH_LONG).show();

                               }
                               else {
                                   OpenWelcomePage(type);
                               }
                           }
                           else{
                               password.setError("Invalid Credentials");
                               password.requestFocus();
                           }
                        }
                        else{
                            username.setError("User does not exist");
                            username.requestFocus();
                        }

                    }

                    @Override
                    public void onCancelled( @NonNull DatabaseError error){}

                                                                 });


                 

                /*
                for(Person x: Users.getUsers()) {
                    if(loginUsername.equals(x.getEmail())) {
                        if(loginPassword.equals((x.getpWord()))) {
                            //sends over the occupation after we confirmed login details
                            found = true;
                            OpenWelcomePage(x.getType());
                        }
                    }

                }
                if(!found) {Toast.makeText(LoginPage.this, "Error: incorrect username/password", Toast.LENGTH_LONG).show();}
                */

            }
        });



    }
    /*

    //checks if user in database
    public void checkUser(){

    }

     */


    //validation checks for inputs 
    public void Checkusername(String email){
        if(!(Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9+_.-]+\\.[A-Za-z]{2,}$").matcher(email).matches())) {
            throw new IllegalArgumentException("Please enter a valid email, ex: example@email.com");
        }
    }

    public void CheckPassword(String password){
        if(!(Pattern.compile("^[A-za-z0-9+_.-]+").matcher(password).matches())){
            throw new IllegalArgumentException("Please enter a password");
        }
    }


    public void OpenWelcomePage(String currentOccupation){
        Intent intent = new Intent(this, Welcomepage.class);
        //passing username data to the next activity
        intent.putExtra("Occupation", currentOccupation);
        startActivity(intent);
    }

}
package com.uottawa.eecs.SEGDeliverable4.main.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.ValueEventListener;
import com.uottawa.eecs.SEGDeliverable4.R;


public class LoginPage extends AppCompatActivity {

    //FirebaseDatabase database;
    //DatabaseReference reference;
    EditText username;
    EditText password;
    MaterialButton loginbutton;

    String currentOccupation;

    String sanitizedUsername;




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

                // if they're the admin just let them in, we don't need to check firebase since there's only one admin account
                if(loginUsername.toLowerCase().equals("user@admin.com") && loginPassword.equals("password")) {
                    username.setError(null); // this prevents any "user does not exist" popups from being displayed
                    password.setError(null);
                    OpenWelcomePage(loginUsername, "Admin");
                    return;
                }

                try {
                    CheckUsername(loginUsername); // make sure its valid input
                    CheckPassword(loginPassword);
                } catch(Exception e) {
                    Toast.makeText(LoginPage.this, "ERROR: " +e.getMessage() , Toast.LENGTH_LONG).show();
                }

                // replace @ and . in the email with _ since it cannot have those in the key
                String sanitizedUsername = loginUsername
                        .replaceAll("[^a-zA-Z0-9]", "_")
                        .toLowerCase();



                // we need to be able to check each of the children that we store the users in
                // however if we just search 'users' it does not check under the others, so it has to be done this way
                DatabaseReference referencePending  = FirebaseDatabase.getInstance().getReference().child("Users").child("Pending");
                DatabaseReference referenceAccepted = FirebaseDatabase.getInstance().getReference().child("Users").child("Accepted");
                DatabaseReference referenceRejected = FirebaseDatabase.getInstance().getReference().child("Users").child("Rejected");

                //checks if there is a child with the email
                referenceAccepted.child(sanitizedUsername).addListenerForSingleValueEvent(new ValueEventListener(){
                    public void onDataChange(@NonNull DataSnapshot snapshot){
                        // first we see if its in accepted so we can just log them in
                        if(snapshot.exists()){
                            username.setError(null);
                            String passwordFromDB = snapshot.child("password").getValue(String.class);

                            if(Objects.equals(passwordFromDB, loginPassword)){
                                username.setError(null);
                                String type = snapshot.child("type").getValue(String.class);
                                OpenWelcomePage(loginUsername,type);
                            }
                            else{
                                password.setError("Invalid Credentials");
                                password.requestFocus();
                            }
                        }
                        else{
                            // check pending
                            referencePending.child(sanitizedUsername).addListenerForSingleValueEvent(new ValueEventListener(){
                                public void onDataChange(@NonNull DataSnapshot snapshot){

                                    if(snapshot.exists()){
                                        username.setError(null);
                                        String passwordFromDB = snapshot.child("password").getValue(String.class);

                                        if(Objects.equals(passwordFromDB, loginPassword)){
                                            Toast.makeText(LoginPage.this, "Unable to login since your request has not been processed", Toast.LENGTH_LONG).show();
                                        }

                                        else{
                                            password.setError("Invalid Credentials");
                                            password.requestFocus();
                                        }
                                    }
                                    else{
                                        // check rejected
                                        referenceRejected.child(sanitizedUsername).addListenerForSingleValueEvent(new ValueEventListener(){
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot){

                                                if(snapshot.exists()){
                                                    username.setError(null);
                                                    String passwordFromDB = snapshot.child("password").getValue(String.class);

                                                    if(Objects.equals(passwordFromDB, loginPassword)) {
                                                        // have to display two toasts since one was too long
                                                        Toast.makeText(LoginPage.this, "Unable to login since your request has been rejected.", Toast.LENGTH_SHORT).show();
                                                        Toast.makeText(LoginPage.this, "Contact admin at 123-456-7890 to resolve this issue.", Toast.LENGTH_LONG).show();

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
                                            // below are required for each of the single value event listeners, but arent used so they remain empty
                                            @Override
                                            public void onCancelled( @NonNull DatabaseError error){}

                                        });


                                    }
                                }
                                @Override
                                public void onCancelled( @NonNull DatabaseError error){}

                            });
                        }

                    }

                    @Override
                    public void onCancelled( @NonNull DatabaseError error){}

                });
            }
        });

    }

    //validation checks for inputs
    public void CheckUsername(String email){
        if(!(Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9+_.-]+\\.[A-Za-z]{2,}$").matcher(email).matches())) {
            throw new IllegalArgumentException("Please enter a valid email, ex: example@email.com");
        }
    }

    public void CheckPassword(String password){
        if(!(Pattern.compile("^[A-za-z0-9+_.-]+").matcher(password).matches())){
            throw new IllegalArgumentException("Please enter a password");
        }
    }


    public void OpenWelcomePage(String userEmail,String currentOccupation){
        Intent intent = new Intent(this, Welcomepage.class);
        //passing username data to the next activity
        intent.putExtra("UserEmail", userEmail);
        Log.e("1", "UserEmail" + userEmail);
        // THIS IS NOT NULL
        //intent.putExtra("SanitizedUsername", sanitizedUsername);
       // ^^ THIS IS NULL
        intent.putExtra("Occupation", currentOccupation);
        startActivity(intent);
    }

}
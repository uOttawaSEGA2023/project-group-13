package com.uottawa.eecs.SEGDeliverable1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.regex.*;


public class RegisterDoctor extends AppCompatActivity {


    //create object of DatabaseReference class to access firebase's Realtime Database
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

    Doctor newDoc = new Doctor("Doctor");
    // array to check if province user entered is correct
    final String[] provinces = {"AB", "BC", "MB", "NB", "NL", "NS", "ON", "PE", "QU", "SK", "NT", "NU", "YT"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_doctor);

        // following code is for specialty check 
        ChipGroup specialty = findViewById(R.id.specialties);

        specialty.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {

          @Override
          public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
              newDoc.clearSpecialties(); // avoiding duplicates 

              for (int k = 0; k < group.getChildCount(); k++) {
                  Chip chip = (Chip) group.getChildAt(k);
                  if (chip.isChecked()) {
                      newDoc.addSpecialty(chip.getText().toString());

                  }
              }
          }
      }
        );
    }

    // what happens when you click register button 
    public void submitOnClick(View view) {
        boolean moveOn, foundProv;
        /*
         * VALIDITY CHECKS FOR USERS INPUT 
         ✓ First Name: can be any string > length 1
         ✓ Last Name: can be any string > length 1
         ✓ Email: should include ***@**.*** 
         ✓ Password: can be any string > length 8
         ✓ Phone number: must be format ###-###-####, integers only
         ✓ Address: street number, street, city, province/territory
         ✓ Employee number: need to decide on a format for it
         ✓ Specialties: hard-coded in, so no need to worry ( need to pick 1 or more)
         *         
         * I consulted here for the regex: https://www.w3schools.com/java/java_regex.asp
         */

        try {

            moveOn = true;
            foundProv = false;

            // check if doesn't contain expression, if so throw error, otherwise set input as the doctor's attribute 
            if(!(Pattern.compile("^[A-za-z ]+").matcher(((EditText) findViewById(R.id.FirstName)).getText().toString())).matches()){
                throw new IllegalArgumentException("Please enter your first name");
            }
            newDoc.setfName(((EditText) findViewById(R.id.FirstName)).getText().toString());

            if(!(Pattern.compile("^[A-za-z ]+").matcher(((EditText) findViewById(R.id.LastName)).getText().toString())).matches()){
                throw new IllegalArgumentException("Please enter your last name");
            }
            newDoc.setlName(((EditText) findViewById(R.id.LastName)).getText().toString());

            if(!(Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9+_.-]+\\.[A-Za-z]{2,}$").matcher(((EditText) findViewById(R.id.Email)).getText().toString()).matches())) {
                throw new IllegalArgumentException("Please enter a valid email, ex: example@email.com");
            }
            newDoc.setEmail(((EditText) findViewById(R.id.Email)).getText().toString());

            if(!(Pattern.compile("^[A-za-z0-9+_.-]{8,}").matcher(((EditText) findViewById(R.id.password)).getText().toString())).matches()){
                throw new IllegalArgumentException("Please enter a password of at least length 8");
            }
            newDoc.setpWord(((EditText) findViewById(R.id.password)).getText().toString());

            if(!(Pattern.compile("^[0-9]{3}-[0-9]{3}-[0-9]{4}").matcher(((EditText) findViewById(R.id.phoneNumber)).getText().toString()).matches())) {
                throw new IllegalArgumentException("Please enter a phone number with format 123-456-7890");
            }
            newDoc.setpNum(((EditText) findViewById(R.id.phoneNumber)).getText().toString());

            if(!(Pattern.compile("^[0-9]+").matcher(((EditText) findViewById(R.id.address)).getText().toString()).matches())){
                throw new IllegalArgumentException("Please enter a valid street number");
            }
            String streetNum = ((EditText) findViewById(R.id.address)).getText().toString();

            if(!(Pattern.compile("^[A-za-z ]+").matcher(((EditText) findViewById(R.id.street)).getText().toString()).matches())) {
                throw new IllegalArgumentException("Please enter a valid street name");
            }
            String streetName = ((EditText) findViewById(R.id.street)).getText().toString();

            if(!(Pattern.compile("^[A-za-z ]+").matcher(((EditText) findViewById(R.id.city)).getText().toString())).matches()) {
                throw new IllegalArgumentException("Please enter your city");
            }
            String city = ((EditText) findViewById(R.id.city)).getText().toString();
            
            //loop through province array to check if its in there 
            for(int i = 0; i < 13; i++) {
                if(provinces[i].equals(((EditText) findViewById(R.id.ProviTeri)).getText().toString().toUpperCase())) {
                    foundProv = true;
                    break;
                }
            }
            if(!foundProv) {
                throw new IllegalArgumentException("Please enter an abbreviated province/territory (eg. AB)");
            }

            String proviterri = ((EditText) findViewById(R.id.ProviTeri)).getText().toString().toUpperCase();
            newDoc.setAddress(streetNum, streetName, city, proviterri);
            
            if(!(Pattern.compile("^[0-9]{10}").matcher(((EditText) findViewById(R.id.employeeNumber)).getText().toString())).matches()){
                throw new IllegalArgumentException("Please enter your 10-digit employee number");
            }
            newDoc.setEmNum(((EditText) findViewById(R.id.employeeNumber)).getText().toString());

            if(newDoc.getSpecialty().isEmpty()) {
                throw new IllegalArgumentException("Please select at least one specialty");
            }

        } catch(Exception e) {
            // we will handle all exceptions the same way: telling the user the issue so they can fix the problematic field
            // since none of the possible issues are app-breaking, its just info we need so they can easily fix it
            Toast.makeText(this, "ERROR: " +e.getMessage() , Toast.LENGTH_LONG).show();
            moveOn = false;
        }
        if(moveOn) {
            newDoc.setStatus(0);


            /*
            databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener(){

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot){
                    //check if email has already been registered before
                    if(snapshot.hasChild(newDoc.getEmail())){
                        Toast.makeText(RegisterDoctor.this, "Email is already registered", Toast.LENGTH_LONG).show();
                    }
                    else{
                        //sending data to firebase Realtime Database
                        //using email as the unique identity of every user
                        databaseReference.child("users").child(newDoc.getEmail()).child("firstName").setValue(newDoc.getfName());
                        databaseReference.child("users").child(newDoc.getEmail()).child("lastName").setValue(newDoc.getlName());
                        databaseReference.child("users").child(newDoc.getEmail()).child("password").setValue(newDoc.getpWord());
                        databaseReference.child("users").child(newDoc.getEmail()).child("phoneNumber").setValue(newDoc.getpNum());
                        databaseReference.child("users").child(newDoc.getEmail()).child("address").setValue(newDoc.getAddress());
                        databaseReference.child("users").child(newDoc.getEmail()).child("employeeNum").setValue(newDoc.getEmNum());
                        databaseReference.child("users").child(newDoc.getEmail()).child("Specialties").setValue(newDoc.getSpecialty());
                        databaseReference.child("users").child(newDoc.getEmail()).child("Status").setValue(newDoc.getStatus());


                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error){

                }
            });



             */


            //Users.updateArrList(newDoc);
            String sanitizedEmail = newDoc.getEmail()
                    .replaceAll("[^a-zA-Z0-9]", "_")
                    .toLowerCase();
            String key = sanitizedEmail;

            databaseReference.child(key).setValue(newDoc);
            // go to main menu
            Toast.makeText(this, "Registration Success!", Toast.LENGTH_LONG).show();
            Intent intent= new Intent(RegisterDoctor.this,MainMenu.class);
            startActivity(intent);
        }
    }
}
package com.uottawa.eecs.SEGDeliverable3;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.List;
import java.util.regex.*;


public class RegisterDoctor extends AppCompatActivity {
    Doctor newDoc = new Doctor("Doctor");
    // array to check if province user entered is correct

    private FirebaseDatabase db =  FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference();
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
              for (int k = 0; k < group.getChildCount(); k++) {
                  Chip chip = (Chip) group.getChildAt(k);
                  if (chip.isChecked()) {
                      String specialtyToAdd = chip.getText().toString();

                      if(!newDoc.getSpecialty().contains(specialtyToAdd)) {
                          newDoc.addSpecialty(specialtyToAdd);
                      }

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

            uploadData();

        }
    }


    public void uploadData() {
        // create a data object to store the information in a way that can be processed regardless of if they're a doctor or patient
        DataClass data = new DataClass(newDoc);

        String sanitizedEmail = newDoc.getEmail()
                .replaceAll("[^a-zA-Z0-9]", "_")
                .toLowerCase();
        String key = sanitizedEmail;

        FirebaseDatabase.getInstance().getReference().child("Users").child("Pending").child(key).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // we uploaded the data! inform the user and return to the main menu
                if(task.isSuccessful()) {
                    Toast.makeText(RegisterDoctor.this, "Registration Success!", Toast.LENGTH_LONG).show();
                    Intent intent= new Intent(RegisterDoctor.this, MainMenu.class);
                    startActivity(intent);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // there was some kind of issue, so tell the user what it was and let them fix it
                Toast.makeText(RegisterDoctor.this, "ERROR: " +e.getMessage() , Toast.LENGTH_LONG).show();
            }
        });

    }
}
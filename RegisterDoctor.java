package com.uottawa.eecs.registrationform;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;
import java.util.regex.*;

public class RegisterDoctor extends AppCompatActivity {
    Doctor newDoc = new Doctor();
    final String[] provinces = {"AB", "BC", "MB", "NB", "NL", "NS", "ON", "PE", "QU", "SK", "NT", "NU", "YT"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_doctor);

        ChipGroup specialty = findViewById(R.id.specialties);

        specialty.setOnCheckedStateChangeListener(new ChipGroup.OnCheckedStateChangeListener() {

            @Override
            public void onCheckedChanged(@NonNull ChipGroup group, @NonNull List<Integer> checkedIds) {
                newDoc.clearSpecialties(); // avoiding duplicates lol

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

    public void submitOnClick(View view) {
        boolean moveOn, foundProv;
        /*
         * need to compare create a way for the inputs to be compared s.t. they follow correct formatting/make sure it's what we actually want
         ✓ First Name: can be any string > length 1
         ✓ Last Name: can be any string > length 1
         ✓ Email: should include ***@**.***
         ✓ Password: can be any string > length 8
         ✓ Phone number: want ***-***-****, integers only, maybe add a toast saying (Want Phone Number in Format 123-456-7890)?
         * Address: input field will probably have to be reworked to get: street number, street, city, province/territory
         ✓ Employee number: need to decide on a format for it
         ✓ Specialties: hard-coded in, so no need to worry
         *
         * want to check to see if email already registered?
         *
         * I consulted here for the regex: https://www.w3schools.com/java/java_regex.asp
         */

        try {

            moveOn = true;
            foundProv = false;

            // don't forget it's !matches since we want to check that it doesn't follow the criteria!!!

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

            ///// HERE ***********
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
            ///// HERE ***********

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
                // we continue
                Toast.makeText(this, "success!" , Toast.LENGTH_LONG).show();
                // go to main menu
            }
    }
}
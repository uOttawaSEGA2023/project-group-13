package com.uottawa.eecs.SEGDeliverable4.main.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.uottawa.eecs.SEGDeliverable4.R;
import com.uottawa.eecs.SEGDeliverable4.main.Address;
import com.uottawa.eecs.SEGDeliverable4.main.DataClass;
import com.uottawa.eecs.SEGDeliverable4.patient.Patient;

import java.util.regex.*;

//import com.google.firebase.auth.FirebaseAuth;

// The same as doctor form, just for the patient instead 
public class RegisterPatient extends AppCompatActivity {

    //create instance variables (based off of text fields)
    EditText FirstName, LastName, Email, Password, Phone, StreetNum, Street, City, ProvTerri, HCNum;
    Button RegisterButton;

    private FirebaseDatabase db =  FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference();

    // array to compare users province
    final String[] provinces = {"AB", "BC", "MB", "NB", "NL", "NS", "ON", "PE", "QU", "SK", "NT", "NU", "YT"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_patient);
       /// fireAuth = FirebaseAuth.getInstance();

        FirstName = findViewById(R.id.FirstName);
        LastName = findViewById(R.id.LastName);
        Email = findViewById(R.id.Email);
        Password = findViewById(R.id.password);
        Phone = findViewById(R.id.phoneNumber);
        StreetNum = findViewById(R.id.streetNum);
        Street = findViewById(R.id.street);
        City = findViewById(R.id.city);
        ProvTerri =findViewById(R.id.ProviTeri);
        HCNum = findViewById(R.id.healthCardNumber);


        RegisterButton = findViewById(R.id.registerButton);


        // what happens if they click the button:
        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean moveOn, foundProv;

                String firstName = FirstName.getText().toString().trim();
                String lastName = LastName.getText().toString().trim();
                String email = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();
                String phone = Phone.getText().toString().trim();

                String streetNum = StreetNum.getText().toString().trim();
                String street = Street.getText().toString().trim();
                String city = City.getText().toString().trim();
                String provTerri = ProvTerri.getText().toString().trim().toUpperCase();

                String healthCardNumber = HCNum.getText().toString().trim();

                try {
                    moveOn = true;
                    foundProv = false;

                    // validity checks (same requirements as doctor form, health card number must be length=10)
                    if(!(Pattern.compile("^[A-za-z ]+").matcher(firstName)).matches()){
                        throw new IllegalArgumentException("Please enter your first name");
                    }

                    if (!(Pattern.compile("^[A-Za-z ]+").matcher(lastName).matches())) {
                        throw new IllegalArgumentException("Please enter your last name");
                    }

                    if (!(Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9+_.-]+\\.[A-Za-z]{2,}$").matcher(email).matches())) {
                        throw new IllegalArgumentException("Please enter a valid email, ex: example@email.com");
                    }

                    if (!(Pattern.compile("^[A-za-z0-9+_.-]{8,}").matcher(password).matches())) {
                        throw new IllegalArgumentException("Please enter a password of at least length 8");
                    }

                    if (!(Pattern.compile("^[0-9]{3}-[0-9]{3}-[0-9]{4}").matcher(phone).matches())) {
                        throw new IllegalArgumentException("Please enter a phone number of format 123-456-7890");
                    }

                    if(!(Pattern.compile("^[0-9]+").matcher(streetNum).matches())){
                        throw new IllegalArgumentException("Please enter a valid street number");
                    }


                    if(!(Pattern.compile("^[A-za-z ]+").matcher(street).matches())) {
                        throw new IllegalArgumentException("Please enter a valid street name");
                    }

                    if(!(Pattern.compile("^[A-za-z ]+").matcher(city).matches())) {
                        throw new IllegalArgumentException("Please enter your city");
                    }

                    for(int i = 0; i < 13; i++) {
                        if(provinces[i].equals(provTerri)) {
                            foundProv = true;
                            break;
                        }
                    }
                    if(!foundProv) {
                        throw new IllegalArgumentException("Please enter an abbreviated province/territory (eg. AB)");
                    }

                    if(!(Pattern.compile("^[0-9]{10}").matcher(healthCardNumber).matches())){
                        throw new IllegalArgumentException("Please enter your 10-digit health card number number");
                    }

                    
                    Address patientAddress = new Address(streetNum,street, city,provTerri);
                    Patient newPatient = new Patient(firstName,lastName,email, password,phone,patientAddress,healthCardNumber,"Patient");
                    //Users.updateArrList(newPatient);

                   /// fireAuth.createUserWithEmailAndPassword(newPatient.getEmail(),newPatient.getpWord());

                } catch(Exception e) {
                    Toast.makeText(RegisterPatient.this, "ERROR: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    moveOn = false;
                }

                // done checks
                if(moveOn) {

                    uploadData();
                }

            }
        });
    }

    public void uploadData() {
        // create a patient and send it to the database
        String firstName = FirstName.getText().toString().trim();
        String lastName = LastName.getText().toString().trim();
        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();
        String phone = Phone.getText().toString().trim();

        String streetNum = StreetNum.getText().toString().trim();
        String street = Street.getText().toString().trim();
        String city = City.getText().toString().trim();
        String provTerri = ProvTerri.getText().toString().trim().toUpperCase();

        String healthCardNumber = HCNum.getText().toString().trim();

        Address patientAddress = new Address(streetNum,street, city,provTerri);
        Patient newPatient = new Patient(firstName,lastName,email, password,phone,patientAddress,healthCardNumber,"Patient");

        DataClass data = new DataClass(newPatient);
        String sanitizedEmail = newPatient.getEmail()
                .replaceAll("[^a-zA-Z0-9]", "_")
                .toLowerCase();
        String key = sanitizedEmail;

        FirebaseDatabase.getInstance().getReference().child("Users").child("Pending").child(key).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // if its successful inform the user and return
                if(task.isSuccessful()) {
                    Toast.makeText(RegisterPatient.this, "Registration Success!", Toast.LENGTH_LONG).show();
                    Intent intent= new Intent(RegisterPatient.this, MainMenu.class);
                    startActivity(intent);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // otherwise it failed for some reason,tell them what it is
                Toast.makeText(RegisterPatient.this, "ERROR: " +e.getMessage() , Toast.LENGTH_LONG).show();
            }
        });

    }

}
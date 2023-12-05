package com.uottawa.eecs.SEGDeliverable4.patient;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uottawa.eecs.SEGDeliverable4.R;
import com.uottawa.eecs.SEGDeliverable4.doctor.shift.Shift;

public class AddApptActivity extends AppCompatActivity {

    String[] specialties = {"Anesthesiology", "Cardiology", "Emergency Medicine", "Family Medicine", "Gastroenterology", "General Surgery",
            "Geriatric Medicine", "Internal Medicine","Neurology", "Oncology", "Pediatrics", "Psychiatry", "Radiology", "Urology"};
    AutoCompleteTextView autoCompleteTextView;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    RecyclerView recyclerView;

    AddApptItemAdapter addApptItemAdapter;

    ArrayAdapter<String> adapterItem;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_appointment);

        // initialize the drop down menu, fill it with the items from "Specialties"
        autoCompleteTextView = findViewById(R.id.autocompletetextview);
        adapterItem = new ArrayAdapter<String>(this, R.layout.appt_list_item_dropdown, specialties);
        autoCompleteTextView.setAdapter(adapterItem);

        // get the recycler view and prepare to fill it
        recyclerView = findViewById(R.id.AddApptRecyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // want to fill the recycler view every time an item is selected from the drop down menu
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // had an issue where the recycler view wouldn't refresh if there were no appointments to be displayed
                // so we clear the adapter
                recyclerView.setAdapter(null);

                // get the item from the drop down
                String item = adapterView.getItemAtPosition(position).toString();

                // firebase doesn't allow spaces in the keys so we had to remove it !
                item = item.replaceAll("\\s+", "");

                // need to look for who has what specialties
                DatabaseReference emailRef = databaseReference.child("Specialties").child(item);
                emailRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot != null) {
                            for (DataSnapshot s : snapshot.getChildren()) {
                                // pull the email
                                TempEmail email = s.getValue(TempEmail.class);
                                String sanitizedEmail = sanitizeEmail(email.getEmail());

                                // we stored the timeslots with shifts, for easy division
                                // check to see if this doctor has any shifts
                                DatabaseReference shiftRef = databaseReference.child("Shifts").child(sanitizedEmail);
                                shiftRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot != null) {

                                            // the doctor does have at least one shift!
                                            for (DataSnapshot s : snapshot.getChildren()) {
                                                // pull each shift
                                                Shift tempShift = s.getValue(Shift.class);

                                                // pull the timeslots from each shift
                                                DatabaseReference timeslotsRef = s.child("timeslots").getRef();

                                                // build the adapter out of these timeslots, for each shift, for each doctor with that specialty
                                                FirebaseRecyclerOptions<Appointment> options =
                                                        new FirebaseRecyclerOptions.Builder<Appointment>()
                                                                .setQuery(timeslotsRef, Appointment.class)
                                                                .build();

                                                // create an adapter
                                                addApptItemAdapter = new AddApptItemAdapter(options);

                                                // set the adapter to the RecyclerView
                                                recyclerView.setAdapter(addApptItemAdapter);

                                                // start listening for data changes only if the adapter is not null (which it shouldn't be - just to be safe)
                                                if (addApptItemAdapter != null) {
                                                    addApptItemAdapter.startListening();
                                                    Log.e("add ! = null", "listening" );
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }

    public String sanitizeEmail(String email) {
        if (email != null) {
            return email.replaceAll("[^a-zA-Z0-9]", "_")
                    .toLowerCase();
        } else {
            // in case of error
            return "user email not showing";
        }
    }
}
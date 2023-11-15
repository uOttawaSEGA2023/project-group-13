package com.uottawa.eecs.SEGDeliverable3;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
//basically the same as the rejected tab just for pending instead

public class UpcomingDoctorApptTab extends Fragment {
    static String username;
    RecyclerView recyclerView;
    UpcomingApptItemAdapter upcomingApptItemAdapter;

    PastApptItemAdapter pastApptItemAdapter;

    static Boolean autoAcceptEnabled = false;

    Switch aptSwitch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View originView =  inflater.inflate(R.layout.fragement_upcoming_doctor_tab, container, false);

        // set username
        Log.e("tag", "BEFORE username is INSIDE UPCOMIONG TAB: " + username);
        username = sanitizeEmail(username); // checking if sanitize works / username is correct
        Log.e("tag", "BEFORE username is INSIDE UPCOMIONG TAB: " + username);

        recyclerView = (RecyclerView) originView.findViewById(R.id.upcomingRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        aptSwitch = (Switch) originView.findViewById(R.id.aptSwitch);
        aptSwitch.setChecked(autoAcceptEnabled);

        // see if autoaccept is enabled
        DatabaseReference autoAcceptRef = FirebaseDatabase.getInstance().getReference().child("Appointments").child(username).child("autoAccept");

        switchTime(); // verify the time of the appointment is not in the past

        autoAcceptRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // get the info from firebase on how to set the autoaccept
                if (dataSnapshot.exists()) {
                    // boolean.class so it knows how to read it
                    autoAcceptEnabled = dataSnapshot.getValue(Boolean.class);
                } else {
                    // false by defualt
                    autoAcceptEnabled = false;
                    autoAcceptRef.setValue(false);
                }

                // set the switch state to whatever was stored in firebase
                aptSwitch.setChecked(autoAcceptEnabled);

                // update the adapter when the switch state changes
                if (upcomingApptItemAdapter != null) {
                    UpcomingApptItemAdapter.setAuto(autoAcceptEnabled);
                    upcomingApptItemAdapter.notifyDataSetChanged();

                    // automatically accept all appointments if they flipped the switch
                    if (autoAcceptEnabled) {
                        acceptAllAppointments();
                        upcomingApptItemAdapter.notifyDataSetChanged();
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // handle error / not needed
            }
        });

        aptSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            autoAcceptEnabled = isChecked;
            Log.e("setauto", "auto is " + isChecked);

            // tell the adapter when the switch state changes
            if (upcomingApptItemAdapter != null) {
                upcomingApptItemAdapter.setAuto(autoAcceptEnabled);
                autoAcceptRef.setValue(autoAcceptEnabled);
                upcomingApptItemAdapter.notifyDataSetChanged(); // refresh the adapter
                //  accept all appointments if auto-accept is enabled
                if (autoAcceptEnabled) {
                    acceptAllAppointments();
                    upcomingApptItemAdapter.notifyDataSetChanged(); // do this at the end so we don't slow the system down
                    // that method is known to be inefficient, so we don't want to call it over and over BUT it's fine for our purposes
                }
            }
        });


        // fills the recycler with the information
        FirebaseRecyclerOptions<Appointment> options =
                new FirebaseRecyclerOptions.Builder<Appointment>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Appointments").child(username).child("Upcoming"), Appointment.class)
                        .build();

        upcomingApptItemAdapter = new UpcomingApptItemAdapter(options);
        recyclerView.setAdapter(upcomingApptItemAdapter); // adapts the pending item into a card that can be displayed
        return originView;
    }


    // these allow us to update in real time from the database

    @Override
    public void onStart() {
        super.onStart();
        upcomingApptItemAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        upcomingApptItemAdapter.stopListening();
    }

    public void setUsername(String u) {
        this.username = u;
    }

    private String sanitizeEmail(String email) {
        if (email != null) {
            return email.replaceAll("[^a-zA-Z0-9]", "_")
                    .toLowerCase();
        } else {
            // in case of error
            return "user email not showing";
        }
    }

    private void acceptAllAppointments() {
        // method to get all of the appointments and set them as accepted
        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference().child("Appointments").child(username).child("Upcoming");

        appointmentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot appointmentSnapshot : dataSnapshot.getChildren()) {
                    // get the appointment
                    Appointment appointment = appointmentSnapshot.getValue(Appointment.class);
                    Log.d("for", "inside for loop with " + appointment.getEmail());

                    // change status to "accepted" for each appointment
                    if (appointment != null) {
                        appointment.setStatus(1);
                        appointmentSnapshot.getRef().setValue(appointment);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // not needed
            }
        });

    }

    private void switchTime(){
        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference().child("Appointments").child(username).child("Upcoming");
        Log.e("USER", "username is " + username);
        appointmentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot appointmentSnapshot : dataSnapshot.getChildren()) {

                    Appointment appointment = appointmentSnapshot.getValue(Appointment.class);
                    Log.d("for", "inside SWITCHTIME loop with " + appointment.getEmail());

                    // change status to "accepted" for each appointment
                    if (appointment != null) {
                        int temp = appointment.getStatus();
                        String aptday = appointment.getDate();
                        String timeEnd = appointment.getEndTime();
                        String date = aptday + " " + timeEnd;
                        Log.d("TIME:", "aptday string: " + aptday);
                        Log.d("TIME:", "timeEnd string: " + timeEnd);
                        Log.d("TIME:", "Date string: " + date);

                        if(temp == 0){ // if their status is 0 we can just delete them
                               if(isStoredDateBeforeCurrent(date)) {
                                   Log.d("TIME:", "INSIDE THE IF STATEMENT"+ date);
                                   String userID = appointmentSnapshot.getKey();
                                   DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Appointments").child(username).child("Upcoming").child(userID);
                                   Log.d("FirebaseRef", "Reference path: " + userRef.toString());
                                   userRef.removeValue();
                                   if (upcomingApptItemAdapter != null) {
                                       Log.d("adp", "inside updating dataset changed");
                                       upcomingApptItemAdapter.notifyDataSetChanged();
                                   }
                               }
                        }
                       if(temp == 1){ // if status is 1 we move it to the past appointment tab
                           if(isStoredDateBeforeCurrent(date)) {
                               String userID =appointmentSnapshot.getKey();
                               DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Appointments").child(username).child("Upcoming").child(userID);
                               Log.d("FirebaseRef", "Reference path: " + userRef.toString());
                               userRef.setValue(null);
                               DatabaseReference newUserRef = FirebaseDatabase.getInstance().getReference().child("Appointments").child(username).child("Past").child(userID);
                               Log.d("FirebaseRef", "Reference path: " + newUserRef.toString());
                               newUserRef.setValue(appointment);
                               if (upcomingApptItemAdapter != null) {
                                   upcomingApptItemAdapter.notifyDataSetChanged();
                               }
                               if (pastApptItemAdapter != null) {
                                   pastApptItemAdapter.notifyDataSetChanged();
                               }

                           }
                       }

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // not needed
                Log.e("ERROR", "Database Error: " + databaseError.getMessage());
            }
        });

    }
    public static boolean isStoredDateBeforeCurrent(String storedDate) {
        SimpleDateFormat firebaseDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm", Locale.US);
        try {
            // parse the stored date string from Firebase
            Date storedDateTime = firebaseDateFormat.parse(storedDate);
            Log.d("TIME", "SOTRED DATE" + storedDateTime.toString());
            // get the current local date and time
            Date currentDateTime = new Date();
            Log.e("TIME", "Current time " + currentDateTime.toString());
            // compare the dates
            Log.e("storedDateTime.before(currentDateTime)", "value:" + storedDateTime.before(currentDateTime));
            return storedDateTime.before(currentDateTime); // returns true if the stored date from firebase is before today
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
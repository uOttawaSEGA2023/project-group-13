package com.uottawa.eecs.SEGDeliverable4.patient;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uottawa.eecs.SEGDeliverable4.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// fragment class representing the rejected tab in the ViewPager2
public class UpcomingPTtab extends Fragment {
    RecyclerView recyclerView;
    static String username, unSaniUsername;
    UpcomingApptItemPTAdapter upcomingApptItemPTAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflates the recycler view, fills it with cards
        View originView =  inflater.inflate(R.layout.fragment_upcoming_patient, container, false);
        recyclerView = (RecyclerView) originView.findViewById(R.id.UpcomingPTRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        //Log.e("BEFORE SANI", username);

        //username = sanitizeEmail(username); // username here is fine, sanitizes properly
        // ^^ i moved that further down lol
        //Log.e("AFTER SANI", username);



        // so username here should also be fine, non-null and we know the path exists
        FirebaseRecyclerOptions<Appointment> options =
                new FirebaseRecyclerOptions.Builder<Appointment>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("PatientAppointments").child(username).child("Upcoming"), Appointment.class)
                        .build();

        upcomingApptItemPTAdapter = new UpcomingApptItemPTAdapter(options);
        recyclerView.setAdapter(upcomingApptItemPTAdapter);

        //code to switch to add activity if button is clicked
        FloatingActionButton fab = originView.findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the AddApptActivity when the FAB is clicked
                Intent intent = new Intent(requireContext(), AddApptActivity.class);
                startActivity(intent);
            }
        });

        return originView;
    }

    // allows it to update in real time
    @Override
    public void onStart() {
        super.onStart();
        upcomingApptItemPTAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        upcomingApptItemPTAdapter.stopListening();
    }

    public void setUsername(String u) {
        unSaniUsername = u;
        this.username = sanitizeEmail(u);
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


    private void switchTime() {
        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference().child("PatientAppointments").child(username).child("Upcoming");
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

                        if (temp == 0) { // if their status is 0 we can just delete them
                            if (isStoredDateBeforeCurrent(date)) {
                                Log.d("TIME:", "INSIDE THE IF STATEMENT" + date);
                                String userID = appointmentSnapshot.getKey();
                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("PatientAppointments").child(username).child("Upcoming").child(userID);
                                Log.d("FirebaseRef", "Reference path: " + userRef.toString());
                                userRef.removeValue();
                                if (upcomingApptItemPTAdapter != null) {
                                    Log.d("adp", "inside updating dataset changed");
                                    upcomingApptItemPTAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                        if (temp == 1) { // if status is 1 we move it to the past appointment tab
                            if (isStoredDateBeforeCurrent(date)) {
                                String userID = appointmentSnapshot.getKey();
                                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("PatientAppointments").child(username).child("Upcoming").child(userID);
                                Log.d("FirebaseRef", "Reference path: " + userRef.toString());
                                userRef.setValue(null);
                                DatabaseReference newUserRef = FirebaseDatabase.getInstance().getReference().child("PatientAppointments").child(username).child("Past").child(userID);
                                Log.d("FirebaseRef", "Reference path: " + newUserRef.toString());
                                newUserRef.setValue(appointment);
                                if (upcomingApptItemPTAdapter != null) {
                                    upcomingApptItemPTAdapter.notifyDataSetChanged();
                                }
                                if (upcomingApptItemPTAdapter != null) {
                                    upcomingApptItemPTAdapter.notifyDataSetChanged();
                                }

                            }
                        }

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
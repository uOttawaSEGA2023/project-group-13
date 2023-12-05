package com.uottawa.eecs.SEGDeliverable4.doctor.appointment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.uottawa.eecs.SEGDeliverable4.patient.Appointment;
import com.uottawa.eecs.SEGDeliverable4.R;
//basically the same as the rejected tab just for pending instead

public class PastDoctorApptTab extends Fragment {

    RecyclerView recyclerView;

    PastApptItemAdapter pastApptItemAdapter;
    String username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View originView =  inflater.inflate(R.layout.fragement_past_doctor_tab, container, false);

        // send everything to the recycler view and inflate it with the cards / correct information
        recyclerView = (RecyclerView) originView.findViewById(R.id.pastRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        Log.e("POTENTIAL USERNAME ERROR", username);
        username = sanitizeEmail(username);
        Log.e("tag", "BEFORE username is INSIDE UPCOMIONG TAB: " + username);

        // fills the recycler with the information
        FirebaseRecyclerOptions<Appointment> options =
                new FirebaseRecyclerOptions.Builder<Appointment>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Appointments").child(username).child("Past"), Appointment.class)
                        .build();

        pastApptItemAdapter = new PastApptItemAdapter(options);
        recyclerView.setAdapter(pastApptItemAdapter); // adapts the pending item into a card that can be displayed
        return originView;
    }


    // these allow us to update in real time from the database

    @Override
    public void onStart() {
        super.onStart();
        pastApptItemAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        pastApptItemAdapter.stopListening();
    }

    // we need this static variable to get the username from other classes
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
}
package com.uottawa.eecs.SEGDeliverable4.patient;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.uottawa.eecs.SEGDeliverable4.R;

// fragment class representing the rejected tab in the ViewPager2
public class PastPTtab extends Fragment {
    RecyclerView recyclerView;
    PastApptItemPTAdapter pastApptItemPTAdapter;
    static String username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflates the recycler view, fills it with cards
        View originView =  inflater.inflate(R.layout.fragment_past_patient, container, false);

        username = sanitizeEmail(username);

        // initialize recycler view, set the adapter

        recyclerView = (RecyclerView) originView.findViewById(R.id.PastPTRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        FirebaseRecyclerOptions<Appointment> options =
                new FirebaseRecyclerOptions.Builder<Appointment>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("PatientAppointments").child(username).child("Past"), Appointment.class)
                        .build();
        pastApptItemPTAdapter = new PastApptItemPTAdapter(options, this);

        recyclerView.setAdapter(pastApptItemPTAdapter);

        return originView;
    }

    // allows it to update in real time
    @Override
    public void onStart() {
        super.onStart();
        pastApptItemPTAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        pastApptItemPTAdapter.stopListening();
    }

    public void setUsername(String u){
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
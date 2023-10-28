package com.uottawa.eecs.SEGDeliverable2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

// fragment class representing the rejected tab in the ViewPager2
public class RejectedTab extends Fragment {

    RecyclerView recyclerView;
    RejectedItemAdapter rejectedItemAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflates the recycler view, fills it with cards
        View originView =  inflater.inflate(R.layout.fragment_rejected_tab, container, false);

        recyclerView = (RecyclerView) originView.findViewById(R.id.RejectedRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        FirebaseRecyclerOptions<DataClass> options =
                new FirebaseRecyclerOptions.Builder<DataClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Users").child("Rejected"), DataClass.class)
                        .build();

        rejectedItemAdapter = new RejectedItemAdapter(options);
        recyclerView.setAdapter(rejectedItemAdapter);

        return originView;
    }

    // allows it to update in real time
    @Override
    public void onStart() {
        super.onStart();
        rejectedItemAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        rejectedItemAdapter.stopListening();
    }
}
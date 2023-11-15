package com.uottawa.eecs.SEGDeliverable3;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
//basically the same as the rejected tab just for pending instead

public class PendingTab extends Fragment {

    RecyclerView recyclerView;
    PendingItemAdapter pendingItemAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View originView =  inflater.inflate(R.layout.fragment_pending_tab, container, false);

        recyclerView = (RecyclerView) originView.findViewById(R.id.PendingRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // fills the recycler with the information
        FirebaseRecyclerOptions<DataClass> options =
                new FirebaseRecyclerOptions.Builder<DataClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Users").child("Pending"), DataClass.class)
                        .build();

        pendingItemAdapter = new PendingItemAdapter(options);
        recyclerView.setAdapter(pendingItemAdapter); // adapts the pending item into a card that can be displayed

       return originView;
    }


    // these allow us to update in real time from the database
    @Override
    public void onStart() {
        super.onStart();
        pendingItemAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        pendingItemAdapter.stopListening();
    }
}
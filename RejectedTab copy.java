package com.uottawa.eecs.SEGDeliverable1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

// fragment class representing the rejected tab in the ViewPager2
public class RejectedTab extends Fragment {

    // to create the content view for this fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         // inflate the layout for this fragment from fragment_rejected_tab.xml
         // the inflated view represents the UI of this fragment
        return inflater.inflate(R.layout.fragment_rejected_tab, container, false);
    }
}
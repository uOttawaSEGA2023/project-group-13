package com.uottawa.eecs.SEGDeliverable4.patient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;
import com.uottawa.eecs.SEGDeliverable4.R;


public class PatientHomePage extends AppCompatActivity {
    TabLayout tabLayout;  // TabLayout for managing tabs
    ViewPager2 viewPager2;// ViewPager2 for handling swipeable function(yeah the tabs are swipeable)
    ViewPagerAdapterPT viewPagerAdapter; // adapter for managing fragments in ViewPager2


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        String username = getIntent().getStringExtra("UserEmail");
        Log.e("PATIENTHOMEPAGE", username);
        // set the content view to the admin homescreen layout
        setContentView(R.layout.patient_homescreen);
        // start TabLayout and ViewPager2 from their respective XML components
        tabLayout = findViewById(R.id.tabLayoutPT);
        viewPager2 = findViewById(R.id.viewPagerPT);
        // initialize  ViewPagerAdapter, passing  current activity as the context
        viewPagerAdapter = new ViewPagerAdapterPT(this);
        // set the adapter for the ViewPager2 to manage fragments
        viewPager2.setAdapter(viewPagerAdapter);
        viewPagerAdapter.setUsername(username);

        // add a listener to the TabLayout for tab selection events

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // when a tab is selected, set the current item of ViewPager2 to the selected tab's position

                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        // Add a callback to ViewPager2 for page change events
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // When a page is selected, select the corresponding tab in TabLayout

                tabLayout.getTabAt(position).select();
            }
        });


    }


}

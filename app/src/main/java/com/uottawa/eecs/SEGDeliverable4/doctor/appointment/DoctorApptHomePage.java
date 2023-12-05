package com.uottawa.eecs.SEGDeliverable4.doctor.appointment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;
import com.uottawa.eecs.SEGDeliverable4.R;


// ok so i think this class is good?
public class DoctorApptHomePage extends AppCompatActivity {

    TabLayout tabLayout;  // tabLayout for managing tabs
    ViewPager2 viewPager2;// viewPager2 for handling swipeable function(yeah the tabs are swipeable)
    ViewPagerAdapterDR viewPagerAdapterDR; // adapter for managing fragments in ViewPager2


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // Set the content view to the admin homescreen layout
        String username = getIntent().getStringExtra("UserEmail");
        Log.e("d", "USER EMAIL: IN DRAPPTHP " + username);
        setContentView(R.layout.doctor_appointment_homescreen);
        // initialize TabLayout and ViewPager2 from their respective XML components
        tabLayout = findViewById(R.id.tabLayoutDR);
        viewPager2 = findViewById(R.id.viewPagerDR);
        // initialize the ViewPagerAdapter, passing the current activity as the context
        viewPagerAdapterDR = new ViewPagerAdapterDR(this);
        viewPagerAdapterDR.setUsername(username);
        // set the adapter for the ViewPager2 to manage fragments
        viewPager2.setAdapter(viewPagerAdapterDR);
        // add a listener to the TabLayout for tab selection events

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //  a tab is selected, set the current item of ViewPager2 to the selected tab's position
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        // add a callback to ViewPager2 for page change events
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // when a page is selected, select the corresponding tab in TabLayout

                tabLayout.getTabAt(position).select();
            }
        });

    }



}

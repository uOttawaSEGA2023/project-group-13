package com.uottawa.eecs.SEGDeliverable1;
// how i learned to make the tabs:
// https://developer.android.com/guide/navigation/navigation-swipe-view
// https://developer.android.com/reference/com/google/android/material/tabs/TabLayout
// https://developer.android.com/guide/navigation/navigation-swipe-view-2

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import android.os.Bundle;
import android.widget.Button;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;

public class AdminHomePage extends AppCompatActivity {

    TabLayout tabLayout;  // TabLayout for managing tabs
    ViewPager2 viewPager2;// ViewPager2 for handling swipeable function(yeah the tabs are swipeable)
    ViewPagerAdapter viewPagerAdapter; // Adapter for managing fragments in ViewPager2
    Button Accept, Reject;


    DatabaseReference databaserUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // Set the content view to the admin homescreen layout
        setContentView(R.layout.admin_homescreen);
        // Initialize TabLayout and ViewPager2 from their respective XML components
        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager);
        // Initialize the ViewPagerAdapter, passing the current activity as the context
        viewPagerAdapter = new ViewPagerAdapter(this);
        // Set the adapter for the ViewPager2 to manage fragments
        viewPager2.setAdapter(viewPagerAdapter);


        // Add a listener to the TabLayout for tab selection events
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // When a tab is selected, set the current item of ViewPager2 to the selected tab's position

                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
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

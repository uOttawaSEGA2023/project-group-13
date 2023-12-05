package com.uottawa.eecs.SEGDeliverable4.patient;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

// adapter class for managing fragments within the ViewPager2
public class ViewPagerAdapterPT extends FragmentStateAdapter {
    // constructor for the ViewPagerAdapter

    String username;
    public ViewPagerAdapterPT(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    // creates a new fragment at the specified position
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.e("IN VIEWPAGERADAPTER PT", username);
        switch(position){
            case 0: {
                UpcomingPTtab u= new UpcomingPTtab();
                u.setUsername(username);
                return u;
            }
            case 1: {
                PastPTtab u= new PastPTtab();
                u.setUsername(username);

                return u;
            }
            default: {
                UpcomingPTtab u= new UpcomingPTtab();
                u.setUsername(username);
                return u;
            }
        }
    }

    // returns the total number of fragments managed by the adapter (in our case, 2)
    @Override
    public int getItemCount() {
        return 2;
    }

    public void setUsername(String u){
        this.username = u;
    }
}

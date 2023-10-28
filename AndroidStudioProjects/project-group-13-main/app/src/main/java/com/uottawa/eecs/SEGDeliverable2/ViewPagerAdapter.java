package com.uottawa.eecs.SEGDeliverable2;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
// adapter class for managing fragments within the ViewPager2
public class ViewPagerAdapter extends FragmentStateAdapter {
    // constructor for the ViewPagerAdapter
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    // creates a new fragment at the specified position
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0: return new PendingTab();
            case 1: return new RejectedTab();
            default:return new PendingTab();
        }
    }

    // returns the total number of fragments managed by the adapter (in our case, 2)
    @Override
    public int getItemCount() {
        return 2;
    }
}

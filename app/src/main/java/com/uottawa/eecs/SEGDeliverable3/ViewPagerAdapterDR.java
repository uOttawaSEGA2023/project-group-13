package com.uottawa.eecs.SEGDeliverable3;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

// i think this class is done???

// adapter class for managing fragments within the ViewPager2
public class ViewPagerAdapterDR extends FragmentStateAdapter {
    // constructor for the ViewPagerAdapter
    String username;
    public ViewPagerAdapterDR(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    // creates a new fragment at the specified position
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0: {
                UpcomingDoctorApptTab u =  new UpcomingDoctorApptTab();
                u.setUsername(username);
                return u;
            }
            case 1: {
                PastDoctorApptTab p = new PastDoctorApptTab();
                p.setUsername(username);
                return p;
            }
            default:  {
                UpcomingDoctorApptTab u =  new UpcomingDoctorApptTab();
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

    public void setUsername(String username) {
        this.username = username;
    }
}

package com.uottawa.eecs.SEGDeliverable3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UpcomingShiftsAdapter extends RecyclerView.Adapter<UpcomingShiftsAdapter.MyViewHolder>{
    Context context;
    ArrayList<UpcomingShiftModel> shiftModels;

    //initialize the adapter with context and data
    public UpcomingShiftsAdapter(Context context, ArrayList<UpcomingShiftModel> shiftModels){
        this.context = context;
        this.shiftModels = shiftModels;
    }

    //called when Recyclerview needs a new ViewHolder to represent an item
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate layout and give a look to each row
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.upcomingshiftrows, parent,false);
        return new MyViewHolder(view);
    }

    //called to display the data at the specified position
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //assigning values to views we created in the activity main file

        holder.date.setText(shiftModels.get(position).getDate());
        holder.time.setText(shiftModels.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        //counts how many items we have
        return shiftModels.size();
    }

    //represents the item view in the recyclerView
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        TextView time;

        //constructor to initialize the views in the viewHolder
        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            date = itemView.findViewById(R.id.showDate);
            time = itemView.findViewById(R.id.showTime);
        }

    }

    //add a new item to the list at the specified position
    public void addItem(UpcomingShiftModel newShift, int position){
        if(shiftModels == null){
            shiftModels = new ArrayList<>();
        }

        if (position >= 0 && position <= shiftModels.size()) {
            shiftModels.add(position, newShift);
            notifyItemInserted(position);
            notifyDataSetChanged();
        } else {
            // Handle the case where the position is out of bounds
            shiftModels.add(newShift);
            notifyItemInserted(shiftModels.size() - 1);
            notifyDataSetChanged();
        }

    }


}

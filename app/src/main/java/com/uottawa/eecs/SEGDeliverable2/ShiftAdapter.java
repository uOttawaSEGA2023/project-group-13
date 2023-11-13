package com.uottawa.eecs.SEGDeliverable2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.ViewHolder> {

    private List<Shift> shifts;


    private OnItemClickListener listener;

    public ShiftAdapter(List<Shift> shifts, OnItemClickListener listener) {
        this.shifts = shifts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shift, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Shift shift = shifts.get(position);
        holder.dateTextView.setText(shift.getDate());
        holder.timeTextView.setText(shift.getStartTime() + " - " + shift.getEndTime());
    }

       // for when a shift is selected
    public interface OnItemClickListener {
        void onItemClick(Shift shift);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView dateTextView;
        TextView timeTextView;
        ViewHolder(View itemView) {
            super(itemView);

            // Set click listener for the item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Shift clickedShift = shifts.get(position);
                        listener.onItemClick(clickedShift);
                    }
                }
            });

            dateTextView = itemView.findViewById(R.id.dateTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
        }
    }


    @Override
    public int getItemCount() {
        return shifts.size();
    }



    public void setShifts(List<Shift> shifts) {
        this.shifts = shifts;
    }



}

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

    public ShiftAdapter(List<Shift> shifts) {
        this.shifts = shifts;
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

    @Override
    public int getItemCount() {
        return shifts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;
        TextView timeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
        }
    }

    public void setShifts(List<Shift> shifts) {
        this.shifts = shifts;
    }

}

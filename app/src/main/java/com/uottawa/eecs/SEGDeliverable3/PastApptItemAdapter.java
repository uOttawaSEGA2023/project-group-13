package com.uottawa.eecs.SEGDeliverable3;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PastApptItemAdapter extends FirebaseRecyclerAdapter<Appointment, PastApptItemAdapter.myViewHolder> {
    // all the logs later can be removed i was just checking stuff ofc
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    public PastApptItemAdapter(@NonNull FirebaseRecyclerOptions<Appointment> options){
        super(options);
    }
    @Override
    protected void onBindViewHolder(@NonNull PastApptItemAdapter.myViewHolder holder, int position, @NonNull Appointment model) {
        String sanitize = model.sanitizeEmail(model.getEmail());
        String drEmail = model.sanitizeEmail(model.getDrEmail());
        Log.e("email", sanitize); // CHECK IF SANITIZE WORKS

        databaseReference.child("Users").child("Accepted").child(sanitize).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            // READS FROM DATABASE AND UPDATES ACCORDINGLY
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("oh no", "on data change!");
                if (dataSnapshot.exists()) { // SEE IF THE PATIENTS EMAIL EXISTS IN ACCEPTED USERS
                    Log.d("oh no", "snap does exist!");
                    DataClass dataClass = dataSnapshot.getValue(DataClass.class);
                    // READ FROM A DATA CLASS SINCE THATS WHAT WE STORE...

                    if (dataClass != null) {
                        holder.firstName.setText(dataClass.getFirstName());
                        holder.lastName.setText((dataClass.getLastName()));
                        holder.email.setText(dataClass.getEmail());
                        holder.phone.setText(dataClass.getPhone());
                        holder.streetNumber.setText(dataClass.getStreetNumber());
                        holder.street.setText(dataClass.getStreet());
                        holder.city.setText(dataClass.getCity());
                        holder.proviTerri.setText(dataClass.getProvince());
                        holder.emHeNum.setText(dataClass.getEmployeeOrHealthCardNumber());
                        Log.d("oh no", "patient does exist!");

                        // PATIENT SHOULD ALWAYS EXIST SINCE ONLY ACCEPTED PATIENTS CAN MAKE APPOINTMENTS
                    } else {
                        // patient is null
                        Log.d("oh no", "patient is null");
                    }
                } else {
                    // patient does not exist D:
                    Log.d("oh no", "patient does not exist!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // firebase has a problem
                Log.e("FirebaseError", "Error: " + databaseError.getMessage());
            }
        });
        holder.dateTime.setText(model.getDate() + " " + model.getStartTime()+ "-" +model.getEndTime()); // add the times and date

        // behavior for the expand button
        holder.expand.setOnClickListener(view -> {
            if(!holder.isExpanded){
                holder.expand.setText("Hide");
                holder.expandableLayout.setVisibility(View.VISIBLE);
                holder.isExpanded = true;
            } else if(holder.isExpanded){
                holder.expand.setText("Expand");
                holder.expandableLayout.setVisibility(View.GONE);
                holder.isExpanded = false;
            }
        });
    }

    @NonNull
    @Override
    public PastApptItemAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.past_appt_item, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        // setting info into the textViews
        ImageView img;
        TextView firstName, lastName, email, phone, streetNumber, street, city, proviTerri, emHeNum, dateTime;

        Button expand;

        Boolean isExpanded;

        public LinearLayout expandableLayout;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            isExpanded = false;
            // get all of the views by their ids
            img = (ImageView) itemView.findViewById(R.id.personIcon);
            firstName = (TextView) itemView.findViewById(R.id.FirstNameText);
            lastName = (TextView) itemView.findViewById(R.id.LastNameText);
            email = (TextView) itemView.findViewById(R.id.EmailText);
            phone = (TextView) itemView.findViewById(R.id.PhoneNumberText);
            streetNumber = (TextView) itemView.findViewById(R.id.StreetNumberText);
            street = (TextView) itemView.findViewById(R.id.StreetText);
            city = (TextView) itemView.findViewById(R.id.CityText);
            proviTerri = (TextView) itemView.findViewById(R.id.ProvinceText);
            emHeNum = (TextView) itemView.findViewById(R.id.NumberText);
            // type = (TextView) itemView.findViewById(R.id.TypeText);
            expand = (Button) itemView.findViewById(R.id.expandButton);
            // specialties = (TextView) itemView.findViewById(R.id.SpecialtiesText);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);
            dateTime = itemView.findViewById(R.id.appointmentDateTime);
        }
    }

}

package com.uottawa.eecs.SEGDeliverable3;

import static java.lang.Integer.parseInt;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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



public class UpcomingApptItemAdapter extends FirebaseRecyclerAdapter<Appointment, UpcomingApptItemAdapter.myViewHolder> {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public static boolean autoAcceptEnabled = false; // Toggle state


    public UpcomingApptItemAdapter(@NonNull FirebaseRecyclerOptions<Appointment> options){
        super(options);
    }





    @Override
    protected void onBindViewHolder(@NonNull UpcomingApptItemAdapter.myViewHolder holder, int position, @NonNull Appointment model) {

        String sanitize = model.sanitizeEmail(model.getEmail());
        String drEmail = model.sanitizeEmail(model.getDrEmail());

        String aptday = model.getDate();
        String timeEnd = model.getEndTime();
        String date = aptday + " " + timeEnd;

        Log.e("email", sanitize); // CHECK IF SANITIZE WORKS

            int temp = model.getStatus();
             Log.d("STATUSSSS"," " + temp);
          if(temp == 1 ){
                Log.d("STATUSSSS", "model is 1");
                holder.accept.setVisibility(View.GONE);
                //holder.reject.setVisibility(View.GONE);
              // ^ we had this hidden but i think it should be visible since we can reject prev. approved

                // status being 1 means that they were approved, so it should be hidden
            }

          //need to get info in yy-MM-dd HH:mm

        Log.d("DATE: ", date);

        databaseReference.child("Users").child("Accepted").child(sanitize).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            // READS FROM DATABASE AND UPDATES ACCORDINGLY
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("oh no", "on data change!");
                if (dataSnapshot.exists()) { // SEE IF THE PATIENTS EMAIL EXISTS IN ACCEPTED USERS
                    Log.d("oh no", "snap does exist!");
                    DataClass dataClass = dataSnapshot.getValue(DataClass.class);
                     // READ FROM A DATA CLASS SINCE THATS WHAT WE STORE... OBVIOUSLY OMG
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

        //button accpted functionality
        holder.accept.setOnClickListener(view -> {
            //accepted

               String userID = getRef(position).getKey();
               model.setStatus(1);
               DatabaseReference userRef = databaseReference.child("Appointments").child(drEmail).child("Upcoming").child(userID);
               userRef.setValue(model);

               holder.accept.setVisibility(View.GONE); // hide the button
        });

        // rejected button
        holder.reject.setOnClickListener(view -> {
            // remove item from cardview and firebase since we dont need to keep track of rejected appointments
            String userID = getRef(position).getKey();
            DatabaseReference userRef = databaseReference.child("Appointments").child(drEmail).child("Upcoming").child(userID);
            userRef.setValue(null);
            notifyItemRemoved(position);
        });}


    @NonNull
    @Override
    public UpcomingApptItemAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcoming_appt_item, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        // set info into textViews
        TextView dateTime, firstName, lastName, email, phone, streetNumber, street, city, proviTerri, emHeNum;

        Button expand, accept, reject;

        Boolean isExpanded;


        public LinearLayout expandableLayout;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            isExpanded = false;
            // get all of the views by their ids
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
            accept = (Button) itemView.findViewById(R.id.AcceptButton);
            reject = (Button) itemView.findViewById(R.id.RejectButton);



        }
    }
    public static void setAuto(Boolean auto) {
        autoAcceptEnabled = auto;
    }

}

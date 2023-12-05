package com.uottawa.eecs.SEGDeliverable4.patient;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uottawa.eecs.SEGDeliverable4.R;

import com.uottawa.eecs.SEGDeliverable4.main.DataClass;



public class AddApptItemAdapter extends FirebaseRecyclerAdapter<Appointment, AddApptItemAdapter.myViewHolder> {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


    public AddApptItemAdapter(@NonNull FirebaseRecyclerOptions<Appointment> options){
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AddApptItemAdapter.myViewHolder holder, int position, @NonNull Appointment model) {

        String drEmail = sanitizeEmail(model.getDrEmail());
        // to get the dr's name and rating ^^

        if(!model.getEmail().equals("null")) {
            // hide the card
            holder.cardview.setVisibility(View.GONE);
            //holder.book.setEnabled(false);
            //holder.book.setText("Booked");
        }

        // need to get a reference to the doctor:
        DatabaseReference doctorReference = databaseReference.child("Users").child("Accepted").child(drEmail);
        doctorReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    DataClass tempDoc = snapshot.getValue(DataClass.class);
                    // set name and rating
                    holder.docName.setText("Dr. " + tempDoc.getFirstName() + " " + tempDoc.getLastName());
                    holder.rating.setText(""+tempDoc.getRating());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // set appointment date and time
        holder.date.setText(model.getDate());
        holder.timeSlot.setText(model.getStartTime() + "-" + model.getEndTime());


        holder.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // need to set the email to the user's email.. oh boy here we go again
                String username = UpcomingPTtab.unSaniUsername;
                String sanitizedUsername = UpcomingPTtab.username;
                String sanitizedDr = sanitizeEmail(model.getDrEmail());

                // ^^^^ is ok to do since a) static and b) we have to view the upcoming tab first
                // so we cant call from the past tab since they may not visit there first
                // but the button is literally on the upcoming tab so its fine


                // set the email to the current user's email so it doesnt get displayed etc
                model.setEmail(username);

                // now the slot is theirs... so we need to put it un Appointments --> upcoming? for dr approval
                String userID = getRef(position).getKey();
                String shiftRef = getRef(position).getParent().toString();
                Log.e("Test", shiftRef); // returns https://d4-base-default-rtdb.firebaseio.com/Shifts/jane_gmail_com/-NkRAyvOCcTMIX2-v9C2/timeslots
                Log.e("test", userID); // returns the index

                // wanna remove this part so we can actually use the path
                String constantPrefix = "https://d4-base-default-rtdb.firebaseio.com";

                // remove that prefix
                shiftRef = shiftRef.substring(constantPrefix.length());

                //Log.e("Test", "Path after /Shifts: " + shiftRef);



                // add it to Appointments under upcoming under doctor
                String pathToNewAppointment = "Appointments/" + sanitizedDr + "/Upcoming";
                // get a reference to the new appointment location
                DatabaseReference newApptRef = databaseReference.child(pathToNewAppointment);

                // push the new appointment data
                DatabaseReference newAppointmentKey = newApptRef.push();
                newAppointmentKey.setValue(model);

                // set the new appointment and change the button so they can't rebook
                databaseReference.child(shiftRef+"/"+userID).setValue(model);
                holder.cardview.setVisibility(View.GONE);
                //holder.book.setEnabled(false);
                //holder.book.setText("Booked");
            }
        });

    }

    @NonNull
    @Override
    public AddApptItemAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // initialze view holder
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeslotitem, parent, false);
        return new myViewHolder(view);
    }

    public String sanitizeEmail(String email) {
        if (email != null) {
            return email.replaceAll("[^a-zA-Z0-9]", "_")
                    .toLowerCase();
        } else {
            // in case of error
            return "user email not showing";
        }



    }

    class myViewHolder extends RecyclerView.ViewHolder{
        // initialize values, set the views to be filled and edited
        TextView docName, timeSlot, date, rating;
        Button book;

        CardView cardview;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            docName = (TextView) itemView.findViewById(R.id.docName);
            timeSlot = (TextView) itemView.findViewById(R.id.TimeSlot);
            date = (TextView) itemView.findViewById(R.id.Date);
            rating = (TextView) itemView.findViewById(R.id.Rating);
            cardview = (CardView) itemView.findViewById(R.id.timeslot);
            book = (Button) itemView.findViewById(R.id.bookButton);
        }


    }


}

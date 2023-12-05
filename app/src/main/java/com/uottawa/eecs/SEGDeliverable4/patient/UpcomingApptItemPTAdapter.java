package com.uottawa.eecs.SEGDeliverable4.patient;

import static com.uottawa.eecs.SEGDeliverable4.patient.UpcomingPTtab.username;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uottawa.eecs.SEGDeliverable4.R;
import com.uottawa.eecs.SEGDeliverable4.main.DataClass;

public class UpcomingApptItemPTAdapter extends FirebaseRecyclerAdapter<Appointment, UpcomingApptItemPTAdapter.myViewHolder> {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


    public UpcomingApptItemPTAdapter(@NonNull FirebaseRecyclerOptions<Appointment> options){
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UpcomingApptItemPTAdapter.myViewHolder holder, int position, @NonNull Appointment model) {

        String patientEmail = model.sanitizeEmail(username);
        String drEmail = model.sanitizeEmail(model.getDrEmail());
        holder.rating.setVisibility(View.GONE); // can't rate appointments we had seen yet lol

        Log.d("EMAIL:","PATIENT: "+ username );

        Log.d("EMAIL:","DOCTOR: "+ drEmail );

        //if the appointment is not approved by the doctor yet we don't want the patient seeing it
        // IF NOTHING SHOWING ITS BECAUSE NO ACCEPTED APPOINTMENTS
         if(model.getStatus() == 0){
           holder.cardView.setVisibility(View.GONE);
        } else {
            holder.cardView.setVisibility(View.VISIBLE);
        }

        holder.date.setText("Date: " + model.getDate());
        holder.timeRange.setText("Time: " + model.getStartTime() + " - " +model.getEndTime());

        DatabaseReference drRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Accepted").child(drEmail);
        drRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    DataClass tempDoctor =  dataSnapshot.getValue(DataClass.class);
                    holder.drName.setText("Dr. " + tempDoctor.getFirstName() + " " + tempDoctor.getLastName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //uhhh error would be handled here in theory
            }
        });

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if the appointment is cancel-able
                // to cancel we need to delete it under the upcoming appointments tab
                // then go to the shift and make it's email "null"

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                try {
                    Date date = format.parse(model.getDate() + " " + model.getStartTime());

                    Date currentDate = new Date(); // gets the current date time

                    long difference = date.getTime() - currentDate.getTime();

                    if(difference > 3600000 ) {
                        // they can delete it!

                        String patientEmail = model.sanitizeEmail(username);
                        String drEmail = model.sanitizeEmail(model.getDrEmail());

                        DatabaseReference patientRef = databaseReference.child("PatientAppointments").child(patientEmail).child("Upcoming");
                        patientRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @SuppressLint("RestrictedApi")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot s: snapshot.getChildren()) {
                                    // want to see if the model is the same as the one there...
                                    // ok so check if everything but email is the same?
                                    if(s.exists()) {
                                        // getting refs to the appointment
                                        String id = getRef(position).toString();
                                        String constantPrefix = "https://d4-base-default-rtdb.firebaseio.com";
                                        id = id.substring(constantPrefix.length());

                                        DatabaseReference shiftRef = databaseReference.child("Shifts").child(drEmail);
                                        shiftRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for(DataSnapshot s: snapshot.getChildren()) {
                                                    for(DataSnapshot t: s.child("timeslots").getChildren()) {
                                                        // THIS GIVES ALL OF THE TIMESLOTS :))
                                                        String tPath = t.getRef().toString();
                                                        tPath = tPath.substring(constantPrefix.length());

                                                        Appointment a = t.getValue(Appointment.class);
                                                        if(checkIfSame(a, model)) {
                                                            // if they are the same we need to set the email for a to null
                                                            a.setEmail("null");
                                                            a.setStatus(0);
                                                            t.getRef().setValue(a);
                                                            // set the values, update firebase and ehre
                                                            getRef(position).setValue(null);
                                                            holder.cardView.setVisibility(View.GONE);

                                                            break;
                                                        }
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    } else {
                        Toast.makeText(v.getContext(), "Unable to delete appointment if it's within one hour.", Toast.LENGTH_LONG).show();
                    }
                } catch (ParseException e) {
                    Log.e("Cancelling appointment", "error cancelling appointment..");
                    throw new RuntimeException(e);
                }
            }
        });

    }

    @NonNull
    @Override
    public UpcomingApptItemPTAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_appt_item, parent, false);
        return new myViewHolder(view);
    }

    private boolean checkIfSame(Appointment a, Appointment s) {
       // this is enough information to determine that they're the same:
        return  a.getDate().equals(s.getDate()) &&
        a.getDrEmail().equals(s.getDrEmail()) &&
        a.getStartTime().equals(s.getStartTime());
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        // set info into textViews/images for cardview
        TextView timeRange, date, drName;
        ImageView img;
        CardView cardView;

        Button rating, cancel;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
           timeRange = (TextView) itemView.findViewById(R.id.timeTextView);
           date = (TextView) itemView.findViewById(R.id.dateTextView);
           drName = (TextView) itemView.findViewById(R.id.DoctorName);
           img = (ImageView) itemView.findViewById(R.id.personIcon);
           cardView = (CardView) itemView.findViewById(R.id.patientCard);
           cancel = (Button) itemView.findViewById(R.id.cancelButton);
           rating = (Button) itemView.findViewById(R.id.addRating);
        }
    }
}


package com.uottawa.eecs.SEGDeliverable3.patient;

import static androidx.core.content.ContentProviderCompat.requireContext;
import static com.uottawa.eecs.SEGDeliverable3.patient.PastPTtab.username;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.uottawa.eecs.SEGDeliverable3.R;
import com.uottawa.eecs.SEGDeliverable3.main.DataClass;

import java.util.ArrayList;

public class PastApptItemPTAdapter extends FirebaseRecyclerAdapter<Appointment, PastApptItemPTAdapter.myViewHolder> {
    private Context context;
    private PopupWindow ratingPopup;
    private RatingBar drRatingBar;
    String drEmail;

    //private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


    public PastApptItemPTAdapter(@NonNull FirebaseRecyclerOptions<Appointment> options, PastPTtab context){
        super(options);
        Log.d("Dataset Size", String.valueOf(options.getSnapshots().size()));

    }

    @Override
    protected void onBindViewHolder(@NonNull PastApptItemPTAdapter.myViewHolder holder, int position, @NonNull Appointment model) {
        //model.getEmail  will return the doctor's email --- so we can get the docs name
        //model.getDrEmail will return the patient ---- we will get the patients email
        Log.d("HERE", "1");
        String patientEmail = model.sanitizeEmail(username);
        Log.d("HERE", "2");
        drEmail = model.sanitizeEmail(model.getEmail());
        Log.d("HERE", "3");

        Log.d("EMAIL:","PATIENT: "+ patientEmail );

        Log.d("EMAIL:","DOCTOR: "+ drEmail );

        holder.date.setText("Date: " + model.getDate());
        holder.timeRange.setText("Time: " + model.getStartTime() + " - " +model.getEndTime());

        DatabaseReference drRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Accepted").child(drEmail);
        drRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    DataClass tempDoctor =  dataSnapshot.getValue(DataClass.class);
                    holder.drName.setText("Dr. " + tempDoctor.getFirstName() + " " + tempDoctor.getLastName());

                    holder.addRating.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                           showRatingPopup(holder,model, drEmail);




                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //uhhh error would be handled here in theory
            }
        });

    }

    private void showRatingPopup(PastApptItemPTAdapter.myViewHolder holder, Appointment appointment, String drEmail) {
        // Initialize the LayoutInflater and inflate the popup layout
        LayoutInflater inflater = LayoutInflater.from(holder.itemView.getContext());
        View popupView = inflater.inflate(R.layout.activity_add_doctor_rating, null);

        // Create the PopupWindow
        PopupWindow ratingPopup = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        // Set up the RatingBar in the popup
        RatingBar drRatingBar = popupView.findViewById(R.id.drRatingBar);

        // Show the popup at a specific position relative to the anchor view (addRating button)
        ratingPopup.showAtLocation(holder.itemView, Gravity.CENTER, 0, 0);

        Button cancelButton = popupView.findViewById(R.id.cancelButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingPopup.dismiss();
            }
        });

        // Handle RatingBar changes
        drRatingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (fromUser) {
                // Handle the rating change


                DatabaseReference allRatingsRef = FirebaseDatabase.getInstance().getReference()
                        .child("Users")
                        .child("Accepted")
                        .child(drEmail)
                        .child("allRatings");

                allRatingsRef.push().setValue(rating);

                // Calculate the average rating
                calculateAverageRating(allRatingsRef,appointment.sanitizeEmail(appointment.getDrEmail()));

                // Dismiss the popup after rating is selected
                ratingPopup.dismiss();
                holder.addRating.setVisibility(View.GONE);
            }
        });
    }

    private void calculateAverageRating(DatabaseReference allRatingsRef, String drEmail) {
        DatabaseReference drRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Accepted").child(drEmail);
        allRatingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    float totalRating = 0;
                    int ratingCount = 0;

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Assuming ratings are stored as floats, adjust if necessary
                        float rating = snapshot.getValue(Float.class);
                        totalRating += rating;
                        ratingCount++;
                    }

                    // Calculate the average rating
                    float averageRating = totalRating / ratingCount;

                    // Update the "rating" attribute with the calculated average
                    drRef.child("rating").setValue(averageRating);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors if necessary
            }
        });

    }






    @NonNull
    @Override
    public PastApptItemPTAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_appt_item, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        // set info into textViews/images for cardview
        TextView timeRange, date, drName;
        ImageView img;
        CardView cardView;

        //RatingBar doctorRatingBar;

        Button addRating;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            //DatabaseReference drRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Accepted").child(drEmail);
            timeRange = (TextView) itemView.findViewById(R.id.timeTextView);
            date = (TextView) itemView.findViewById(R.id.dateTextView);
            drName = (TextView) itemView.findViewById(R.id.DoctorName);
            img = (ImageView) itemView.findViewById(R.id.personIcon);
            cardView  = (CardView) itemView.findViewById(R.id.patientCard);
            //doctorRatingBar = itemView.findViewById(R.id.doctorRatingBar);

            addRating = (Button)itemView.findViewById(R.id.addRating);
            //addRating.setOnClickListener(new View.OnClickListener() {

        }


    }
        }









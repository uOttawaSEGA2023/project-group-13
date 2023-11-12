package com.uottawa.eecs.SEGDeliverable2;

// DoctorShiftsActivity.java
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import com.google.firebase.auth.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import android.content.Intent;



public class DoctorShiftsActivity extends AppCompatActivity implements ShiftAdapter.OnItemClickListener{

    private RecyclerView recyclerView;
    private ShiftAdapter shiftAdapter;
    private List<Shift> shifts;
    private DatabaseReference shiftsRef;

    private String userEmail;
    String sanitizeEmail;

    private String key;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_shifts);

        shifts = new ArrayList<>();
        shiftAdapter = new ShiftAdapter(shifts, new ShiftAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Shift shift) {
                showUpdateDeleteDialog(shift);

            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(shiftAdapter);


        Intent intent = getIntent();
        if (intent != null) {
             userEmail = intent.getStringExtra("UserEmail");
             }

        //clean email
        sanitizeEmail = sanitizeEmail(userEmail);




        shiftsRef = FirebaseDatabase.getInstance().getReference("Shifts");

        Log.d("Debug", "User email: " + userEmail);
        Log.d("Debug", "shiftsRef: " + shiftsRef);
        Log.d("Debug", "sanitize email " + sanitizeEmail);

        Button addShiftButton = findViewById(R.id.addShiftButton);
        addShiftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddShiftDialog();
            }
        });

        loadShifts();
    }

    private void showAddShiftDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_shift);

        final EditText dateEditText = dialog.findViewById(R.id.dateEditText);
        final EditText startTimeEditText = dialog.findViewById(R.id.startTimeEditText);
        final EditText endTimeEditText = dialog.findViewById(R.id.endTimeEditText);
        Button saveButton = dialog.findViewById(R.id.saveButton);

        // Set current date as default in the dateEditText
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dateEditText.setText(dateFormat.format(Calendar.getInstance().getTime()));

        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker(dateEditText);
            }
        });

        startTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(startTimeEditText);
            }
        });

        endTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(endTimeEditText);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = dateEditText.getText().toString();
                String startTime = startTimeEditText.getText().toString();
                String endTime = endTimeEditText.getText().toString();

                if (!date.isEmpty() && !startTime.isEmpty() && !endTime.isEmpty()) {
                    // Create a new Shift object
                    Shift newShift = new Shift(userEmail, date, startTime, endTime);

                    // Add the new shift to Firebase
                    saveShiftToFirebase(newShift);

                    // Refresh the shift list
                    loadShifts();

                    // Dismiss the dialog
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }


    private void loadShifts() {

        shiftsRef.child(sanitizeEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                shifts.clear();
                for (DataSnapshot shiftSnapshot : dataSnapshot.getChildren()) {
                    Shift shift = shiftSnapshot.getValue(Shift.class);
                    if (shift != null) {
                        shifts.add(shift);
                    }
                }

                // Set the updated list to the adapter
                shiftAdapter.setShifts(shifts);

                // Notify the adapter that the data set has changed
                shiftAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });
    }



    private void saveShiftToFirebase(Shift newShift) {
        String sanitizedEmail = sanitizeEmail(userEmail);
        DatabaseReference shiftsUserRef = shiftsRef.child(sanitizedEmail);

        // Generates the key to use to delete later on
        key = shiftsUserRef.push().getKey();

        // Save the Shift object with the key to Firebase
        shiftsUserRef.child(key).setValue(newShift);
    }





    private void showDatePicker(final EditText dateEditText) {
        final Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        currentDate.set(Calendar.YEAR, year);
                        currentDate.set(Calendar.MONTH, monthOfYear);
                        currentDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        dateEditText.setText(dateFormat.format(currentDate.getTime()));
                    }
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void showTimePicker(final EditText timeEditText) {
        final Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        currentTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        currentTime.set(Calendar.MINUTE, minute);

                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.US);
                        timeEditText.setText(timeFormat.format(currentTime.getTime()));
                    }
                },
                hour, minute, true // Set to true if you want 24-hour format
        );
        timePickerDialog.show();
    }

    private String sanitizeEmail(String email) {
        if (email != null) {
            return email.replaceAll("[^a-zA-Z0-9]", "_")
                    .toLowerCase();
        } else {
            // Handle the case where email is null, return a default value or throw an exception
            return "user email not showing";
        }
    }


    private void deleteShift(Shift shift) {
        // Delete the shift from Firebase using the key from the Shift object
        String sanitizedEmail = sanitizeEmail(userEmail);
        DatabaseReference deleteshiftRef = shiftsRef.child(sanitizedEmail).child(key);
        deleteshiftRef.removeValue();

        // Refresh the shift list
        loadShifts();
    }

    private void showUpdateDeleteDialog(Shift shift) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.delete_dialog, null);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteProduct);
        dialogBuilder.setView(dialogView);


        final AlertDialog b = dialogBuilder.create();
        b.show();



        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteShift(shift);
                b.dismiss();
            }
        });
    }


    @Override
    public void onItemClick(Shift shift) {
        showUpdateDeleteDialog(shift);

    }
}


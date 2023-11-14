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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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
import android.widget.Toast;
import java.util.UUID;


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

        key = shiftsRef.child(sanitizeEmail).push().getKey();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = dateEditText.getText().toString();
                String startTime = startTimeEditText.getText().toString();
                String endTime = endTimeEditText.getText().toString();

                if (!date.isEmpty() && !startTime.isEmpty() && !endTime.isEmpty()) {
                    // Create a new Shift object
                    Shift newShift = new Shift(userEmail, date, startTime, endTime);

                    if (!isDateValid(date)) {
                        // Show an error message or toast indicating that the selected date has already passed
                        Toast.makeText(DoctorShiftsActivity.this, "Please select a future date.", Toast.LENGTH_SHORT).show();
                        return;

                        //checking to see if the shift is conflicting with another one
                    } else if (!shiftConflict(newShift)) {
                        // Save the new shift to Firebase
                        saveShiftToFirebase(newShift);

                        // Refresh the shift list
                        loadShifts();

                        // Dismiss the dialog
                        dialog.dismiss();
                        }

                    if (isDateValid(date)) {
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.US);

                        Date startTimeDate = null;
                        try {
                            startTimeDate = timeFormat.parse(startTime);
                            Date endTimeDate = timeFormat.parse(endTime);
                            long durationMillis = endTimeDate.getTime() - startTimeDate.getTime();
                            int durationMinutes = (int) (durationMillis / (60 * 1000));

                            if (!checkInterval(durationMinutes)) {

                                Toast.makeText(DoctorShiftsActivity.this, "Please enter a duration of 30 minute interval.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }


                    }

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
            // in case of error
            return "user email not showing";
        }
    }




    private void deleteShift(Shift shift) {
        // Delete the shift from Firebase using the provided key
        try{
            String sanitizedEmail = sanitizeEmail(userEmail);
            String delKey = shift.getKey();
            DatabaseReference deleteshiftRef = shiftsRef.child(sanitizedEmail).child(delKey);
            deleteshiftRef.removeValue();
            shiftAdapter.notifyDataSetChanged();

            loadShifts();
        }catch (Exception exception){
            exception.printStackTrace();
        }

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


    private boolean isDateValid(String selectedDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date currentDate = new Date();
            Date selected = dateFormat.parse(selectedDate);

            // if in the past returns false
            return (!selected.before(currentDate));
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean checkInterval(int duration){
        if(duration % 30 == 0){
            return true;
        }
        else{
            return false;
        }

    }

    private boolean shiftConflict(Shift newShift) {

        for (Shift shift : shifts) {
            //create variables to parse the strings into a valid date objects

            String newShiftGST = newShift.getStartTime();
            String newShiftGET = newShift.getEndTime();
            String shiftGST = shift.getStartTime();
            String shiftGET = shift.getEndTime();

            // Check if the new shift's date overlaps with any existing shift
            if (newShift.getDate().equals(shift.getDate())) {
                // Parsing variables
                SimpleDateFormat time = new SimpleDateFormat("HH:mm", Locale.US);
                try {
                    Date newStartTime = time.parse(newShiftGST);
                    Date newEndTime = time.parse(newShiftGET);
                    Date shiftStartTime = time.parse(shiftGST);
                    Date shiftEndTime = time.parse(shiftGET);

                    // Check to see if new shift starts before existing shifts end time and
                    // if the new shift ends after the existing shifts start time
                    if ((newStartTime.before(shiftEndTime)) && (newEndTime.after(shiftStartTime))) {
                        // If there is a conflict then show this message and return true
                        Toast.makeText(DoctorShiftsActivity.this, "Shift conflict: select another date and time", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                } catch (ParseException exception) {
                    exception.printStackTrace();
                }
            }
        }
        // If no conflicts are found then the boolean is not true
        return false;
    }
}


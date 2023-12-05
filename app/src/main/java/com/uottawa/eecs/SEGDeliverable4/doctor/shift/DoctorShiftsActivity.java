package com.uottawa.eecs.SEGDeliverable4.doctor.shift;

// DoctorShiftsActivity.java
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uottawa.eecs.SEGDeliverable4.R;
import com.uottawa.eecs.SEGDeliverable4.main.DataClass;
import com.uottawa.eecs.SEGDeliverable4.patient.Appointment;

import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import android.content.Intent;
import android.widget.Toast;


public class DoctorShiftsActivity extends AppCompatActivity implements ShiftAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private ShiftAdapter shiftAdapter;
    private List<Shift> shifts;
    private DatabaseReference shiftsRef;

    private String userEmail;
    String sanitizeEmail;

    private String key;

    public String specialties;

    private String shiftDate;



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
            userEmail = intent.getStringExtra("UserEmail"); // pull the user info from the previous activity
        }

        //clean email
        sanitizeEmail = sanitizeEmail(userEmail);




        shiftsRef = FirebaseDatabase.getInstance().getReference("Shifts"); // get ref to firebase

        Log.d("Debug", "User email: " + userEmail);
        Log.d("Debug", "shiftsRef: " + shiftsRef);
        Log.d("Debug", "sanitize email " + sanitizeEmail); // making sure info is saved correctly

        Button addShiftButton = findViewById(R.id.addShiftButton);
        addShiftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddShiftDialog();
            }
        });

        loadShifts(); // show the shifts to the user
    }

    private void showAddShiftDialog() {
        // method to display the UI to add a shift
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
            // when clicking on the date textbox, open UI to choose a date
            @Override
            public void onClick(View v) {
                showDatePicker(dateEditText);
            }
        });

        // when clicking on either time, open the UI to select a time
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

        key = shiftsRef.child(sanitizeEmail).push().getKey(); // get the key to store in firebase / with the shift

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the values the user inputted
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
                    }
                    if (shiftConflict(newShift)) {
                        // the method displays its own toast, so just return
                        return;

                    }

                    if (isDateValid(date)) {
                        // if the date is ok to proceed, then check the time
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.US);

                        Date startTimeDate = null;
                        try {
                            startTimeDate = timeFormat.parse(startTime);
                            Date endTimeDate = timeFormat.parse(endTime);
                            long durationMillis = endTimeDate.getTime() - startTimeDate.getTime();
                            long durationMinutes =  (durationMillis / (60 * 1000));

                            // if its not some multiple of 30 inform the user and return
                            if (!checkInterval(durationMinutes)) {
                                Toast.makeText(DoctorShiftsActivity.this, "Please enter a duration of 30 minute interval.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }


                    }

                    // meets all requirements, make the shift, refresh recycler view and send to firebase
                    saveShiftToFirebase(newShift);
                    // Refresh the shift list
                    loadShifts();
                    // Dismiss the dialog
                    dialog.dismiss(); // we dont need the UI anymore

                }
            }

        });

        dialog.show();
    }


    private void loadShifts() {
        // method to display shifts
        shiftsRef.child(sanitizeEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                shifts.clear();
                for (DataSnapshot shiftSnapshot : dataSnapshot.getChildren()) { // get all the data snapshots / shifts from firebase
                    Shift shift = shiftSnapshot.getValue(Shift.class); // convert them to a shift for local use
                    if (shift != null) {
                        shifts.add(shift); // add shifts to the array of shifts to be displayed / read into the recycler view
                    }
                }

                // Set the updated list to the adapter
                shiftAdapter.setShifts(shifts);

                // Notify the adapter that the data set has changed
                shiftAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // not needed
            }
        });
    }



    private void saveShiftToFirebase(Shift newShift) {
        String sanitizedEmail = sanitizeEmail(userEmail);
        DatabaseReference shiftsUserRef = shiftsRef.child(sanitizedEmail);
        // Generates the key to use to delete later on
        key = shiftsUserRef.push().getKey();
        // for D4: create an array of times,
        createTimeslots(newShift, new FireBaseCallBack() {
            @Override
            public void onCallBack() {
                shiftsUserRef.child(key).setValue(newShift); // save it under this key
                shiftsUserRef.child(key).child("key").setValue(key); // have to push as a key attribute?
                loadShifts();
            }
        });

    }



    private Shift createTimeslots(Shift shift, FireBaseCallBack callBack) {

        // calculate number of timeslots
        String startTime = shift.getStartTime();
        String endTime = shift.getEndTime();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.US);
        int count = 0;
        long durationMinutes;

        Date startTimeDate = null;
        try {
            startTimeDate = timeFormat.parse(startTime);
            Date endTimeDate = timeFormat.parse(endTime);
            long durationMillis = endTimeDate.getTime() - startTimeDate.getTime();
            durationMinutes =  (durationMillis / (60 * 1000));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        int numBlocks = ((int) durationMinutes) / 30;
        shift.setTimeslots(new ArrayList<Appointment>(numBlocks));
        String date = shift.getDate();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Accepted").child(sanitizeEmail(userEmail));

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    DataClass doc = snapshot.getValue(DataClass.class);
                    Log.d("Debug", "Specialties from onDataChange: " + doc.getSpecialties());
                    specialties = doc.getSpecialties();
                    Log.d("Debug", specialties);


                    DateTimeFormatter formatter = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.US);

                        String tempStartTime = shift.getStartTime();

                        Log.d("Debug", specialties);



                        for (int i = 0; i < numBlocks; i++) {
                            // logic to increment start
                            // logic to increment end

                            LocalTime time = LocalTime.parse(tempStartTime, formatter);
                            time = time.plusMinutes(30 * i);
                            LocalTime endTimeTemp = time.plusMinutes(30);
                            String startTime = (time.toString());
                            String endTime = endTimeTemp.toString();




                            shift.addTimeslots (new Appointment("null", startTime, endTime, date, userEmail, specialties));
                            callBack.onCallBack();
                        }
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // error occurred!
            }
        });


        Log.e("RETURN", "ABOUT TO RETURN SHIFT");
        return shift;
    }

    private void showDatePicker(final EditText dateEditText) {
        // displays date picker UI
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

                        // formats the information into the date picker
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        dateEditText.setText(dateFormat.format(currentDate.getTime()));
                    }
                },
                year, month, day
        );
        datePickerDialog.show(); // displays the UI
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

                        // formats the information into the time picker
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.US);
                        timeEditText.setText(timeFormat.format(currentTime.getTime()));
                    }
                },
                hour, minute, true // Set to true if you want 24-hour format
        );
        timePickerDialog.show();
    }

    private String sanitizeEmail(String email) {
        // needed since we can't have @ or . in keys from firebase
        if (email != null) {
            return email.replaceAll("[^a-zA-Z0-9]", "_")
                    .toLowerCase();
        } else {
            // in case of error
            return "user email not showing";
        }
    }




//    private void deleteShift(Shift shift) {
//        // Delete the shift from Firebase using the provided key
//        try{
//            String sanitizedEmail = sanitizeEmail(userEmail);
//            String delKey = shift.getKey(); // calling getKey on a shift (not a DatabaseReference!), so that's why we need the key saved as an attribute...
//            DatabaseReference deleteshiftRef = shiftsRef.child(sanitizedEmail).child(delKey);
//            deleteshiftRef.removeValue();
//            shiftAdapter.notifyDataSetChanged();
//
//            loadShifts();
//        }catch (Exception exception){
//            exception.printStackTrace();
//        }
//
//    }

    private void showUpdateDeleteDialog(Shift shift) {
        // create an alert dialog to confirm deletion of a shift
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.delete_dialog, null);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteProduct);
        dialogBuilder.setView(dialogView);

        //display it
        final AlertDialog b = dialogBuilder.create();
        b.show();

        //dismiss it when they tap outside / press confirm delete button
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
        // prompt if they actually want to delete whenever they select an item
    }


    private boolean isDateValid(String selectedDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US); // format the date
            Date currentDate = new Date();
            Date selected = dateFormat.parse(selectedDate);
            // if in the past returns false
            return (!selected.before(currentDate));
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean checkInterval(long duration){ // duration is a long IN MINUTES
        // verify that it is a multiple of 30 minutes
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

    private interface FireBaseCallBack {
        void onCallBack();
    }


    private void deleteShift(Shift shift) {
        DatabaseReference appointmentsRef = FirebaseDatabase.getInstance().getReference().child("Appointments");
        shiftDate=shift.getDate();
        appointmentsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean canDelete = true;
                for (DataSnapshot patientSnapshot : dataSnapshot.getChildren()) {
                    // Loop through patient's appointments
                    for (DataSnapshot appointmentSnapshot : patientSnapshot.child("Upcoming").getChildren()) {
                        String drEmail = appointmentSnapshot.child("drEmail").getValue(String.class);
                        String date = appointmentSnapshot.child("date").getValue(String.class);
                        String endTime = appointmentSnapshot.child("endTime").getValue(String.class);
                        String startTime = appointmentSnapshot.child("startTime").getValue(String.class);

                        // Compare if the appointment matches the shift's date and time
                        //convertDate of shift format yyyy-mm-dd to appointment style yy-mm-dd
                        if (drEmail != null && date != null && endTime != null && startTime != null) {
                            if (drEmail.equals(shift.getUserEmail()) && date.equals(convertDateToNewFormat(shiftDate))
                                    && doTimesConflict(startTime, endTime, shift.getStartTime(), shift.getEndTime())) {
                                // If there's a conflict, prevent shift deletion
                                canDelete = false;
                                break;
                            }
                        }
                    }
                    if (!canDelete) {
                        break;
                    }
                }

                if (canDelete) {
                    // Allow deletion of the shift
                    try{
                        String sanitizedEmail = sanitizeEmail(userEmail);
                        String delKey = shift.getKey(); // calling getKey on a shift (not a DatabaseReference!), so that's why we need the key saved as an attribute...
                        DatabaseReference deleteshiftRef = shiftsRef.child(sanitizedEmail).child(delKey);
                        deleteshiftRef.removeValue();
                        shiftAdapter.notifyDataSetChanged();

                        loadShifts();
                    }catch (Exception exception){
                        exception.printStackTrace();
                    }
                } else {
                    // toast message that indicates doctor cannot delete shift
                    Toast.makeText(DoctorShiftsActivity.this, "Cannot delete. Appointments already booked with this shift.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error if needed
            }
        });
    }

    private boolean doTimesConflict(String startTime1, String endTime1, String startTime2, String endTime2) {
        try {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.US);

            // Parse the time strings into Date objects
            Date start1 = timeFormat.parse(startTime1);
            Date end1 = timeFormat.parse(endTime1);
            Date start2 = timeFormat.parse(startTime2);
            Date end2 = timeFormat.parse(endTime2);

            // Check for overlap: if one start time is before the other end time, there's an overlap
            return (start1.before(end2) && end1.after(start2));
        } catch (ParseException e) {
            e.printStackTrace();
            return false; // Return false in case of a parsing error or invalid times
        }
    }


    // used to convert shift date format yyyy-mm-dd to appointment format yy-mm-dd
    public static String convertDateToNewFormat(String originalDateStr) {
        try {
            // Parse the original date string into a Date object
            SimpleDateFormat originalDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date originalDate = originalDateFormat.parse(originalDateStr);

            // Format the Date object into the desired format ("yy-MM-dd")
            SimpleDateFormat desiredDateFormat = new SimpleDateFormat("yy-MM-dd", Locale.US);
            return desiredDateFormat.format(originalDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Return null in case of a parsing error
        }
    }


}



package com.uottawa.eecs.SEGDeliverable2;

// DoctorShiftsActivity.java
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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


public class DoctorShiftsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ShiftAdapter shiftAdapter;
    private List<Shift> shifts;
    private DatabaseReference shiftsRef;
    private FirebaseAuth mAuth;
    private String doctorId; // Add a variable to store the doctor's ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_shifts);

        shifts = new ArrayList<>();
        shiftAdapter = new ShiftAdapter(shifts);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(shiftAdapter);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            doctorId = currentUser.getUid();
        }

        shiftsRef = FirebaseDatabase.getInstance().getReference().child("doctors").child(doctorId).child("shifts");

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
                    Shift newShift = new Shift(doctorId, date, startTime, endTime);

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
        shiftsRef.addListenerForSingleValueEvent(new ValueEventListener() {
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

    // ... (the rest of your code remains unchanged)

    private void saveShiftToFirebase(Shift newShift) {
        shiftsRef.push().setValue(newShift);
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

}

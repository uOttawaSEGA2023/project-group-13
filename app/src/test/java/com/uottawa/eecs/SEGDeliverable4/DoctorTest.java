package com.uottawa.eecs.SEGDeliverable4;

import static org.junit.Assert.assertEquals;

import com.uottawa.eecs.SEGDeliverable4.doctor.Doctor;

import org.junit.Test;

import java.util.ArrayList;

public class DoctorTest {

    @Test
    public void testDoctorGetRating() {
        ArrayList<String> specialties = new ArrayList<>();
        specialties.add("Anesthesiology");
        Doctor doctor = new Doctor("Jane", "Doe", "jane@email.com", "janepass", "123-456-7890", "0987654321", specialties, "Doctor",4);
        float actual = doctor.getRating();
        float expected = 4;
        assertEquals(expected, actual, 0);
    }
}

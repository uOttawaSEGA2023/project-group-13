package com.uottawa.eecs.SEGDeliverable4;

import static org.junit.Assert.assertEquals;

import com.uottawa.eecs.SEGDeliverable4.main.Address;
import com.uottawa.eecs.SEGDeliverable4.patient.Patient;

import org.junit.Test;

public class PatientTest {

    @Test
    public void testPatientGetHealthCardNumber() {
        Address address = new Address("64", "Pineapple St", "Calgary", "AB");
        Patient patient = new Patient("John", "Smith", "jsmith@email.com", "johnpass", "123-456-7890", address, "1234567890","Patient");
        String hNum = patient.getHealthCardNumber();
        String expected = "1234567890";
        assertEquals(expected, hNum);
    }

    @Test
    public void testPatientRegistrationStatus() {
        Address address = new Address("64", "Pineapple St", "Calgary", "AB");
        Patient patient = new Patient("John", "Smith", "jsmith@email.com", "johnpass", "123-456-7890", address, "1234567890","Patient");
        patient.setRegistrationStatus(1);
        int status = patient.getRegistrationStatus();
        int expected = 1;
        assertEquals(expected, status);
    }
}

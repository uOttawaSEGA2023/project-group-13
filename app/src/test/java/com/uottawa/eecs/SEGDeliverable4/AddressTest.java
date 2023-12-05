package com.uottawa.eecs.SEGDeliverable4;

import static org.junit.Assert.assertNotEquals;

import com.uottawa.eecs.SEGDeliverable4.main.Address;

import org.junit.Test;

public class AddressTest {

    @Test
    public void testAddressCity() {
        Address address = new Address();
        address.setCity("Brantford");
        String city = address.getCity();
        assertNotEquals("Bradford", city);
    }
}

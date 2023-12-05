package com.uottawa.eecs.SEGDeliverable4;

import static org.junit.Assert.assertNotEquals;

import com.uottawa.eecs.SEGDeliverable4.admin.Admin;

import org.junit.Test;

public class AdminTest {
    @Test
    public void testAdminStatus() {
        Admin admin = new Admin("admin1", "safepass");
        int status = admin.getRegistrationStatus();
        assertNotEquals(0, status);
    }
}

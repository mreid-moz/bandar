package com.mozilla.bandar.health;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.mozilla.bandar.Constants;
import com.yammer.metrics.core.HealthCheck.Result;

public class TestBaseDirHealthCheck {

    @Test
    public void test() {
        BaseDirHealthCheck health = new BaseDirHealthCheck(Constants.VALID_FILE_PROVIDER_PATH);
        try {
            Result result = health.check();
            assertTrue(result.isHealthy());
        } catch(Exception e) {
            fail("Should not have thrown.");
        }

        health = new BaseDirHealthCheck(Constants.INVALID_FILE_PROVIDER_PATH);
        try {
            Result result = health.check();
            assertFalse(result.isHealthy());
        } catch(Exception e) {
            fail("Should not have thrown.");
        }
    }
}

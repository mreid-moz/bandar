package com.mozilla.bandar.dnt;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class TestData {

    @Test
    public void test() {
    	Data data = new Data("DESKTOP", "DAILY", "data/");
    	String jsonData = data.displayDailyNumbers("DESKTOP");
        System.err.println("data: " + jsonData);
        assertNotNull(jsonData);
    }
}

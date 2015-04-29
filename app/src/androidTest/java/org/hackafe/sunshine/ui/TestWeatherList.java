package org.hackafe.sunshine.ui;

import android.test.ActivityInstrumentationTestCase2;

import org.hackafe.sunshine.MainActivity;

/**
 * Created by groupsky on 29.04.15.
 */
public class TestWeatherList extends ActivityInstrumentationTestCase2 {

    public TestWeatherList() {
        super(MainActivity.class);
    }

    public void _testFailure() {
        assertTrue(false);
    }
}

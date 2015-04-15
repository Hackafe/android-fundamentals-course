package org.hackafe.sunshine;

import android.test.AndroidTestCase;

/**
 * Created by groupsky on 15.04.15.
 */
public class TestPractice extends AndroidTestCase {

    public void testStringEquals() throws Exception {
        String a = "abcdef";
        String b = "qwerty";

        assertEquals(a, b);
    }

}

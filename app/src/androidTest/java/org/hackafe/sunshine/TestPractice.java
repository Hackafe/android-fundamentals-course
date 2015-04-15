package org.hackafe.sunshine;

import android.test.AndroidTestCase;

/**
 * Created by groupsky on 15.04.15.
 */
public class TestPractice extends AndroidTestCase {

    public void testStringEquals() throws Exception {
        String a = "hackafe";
        String b = "hackafe";

        assertEquals(a, b);
    }

    public void testIntegersNotEquals() throws Exception {
        int expected = 5;
        int actual = 3;

        if (expected == actual)
            fail("but they are equals :'(");
    }

    public void testExceptions() throws Exception {
        Object a = null;
        try {
            a.toString();
            fail("where's my exception, uaaa");
        } catch (NullPointerException e) {

        }
    }
}

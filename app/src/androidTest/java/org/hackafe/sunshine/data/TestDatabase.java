package org.hackafe.sunshine.data;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

/**
 * Created by groupsky on 15.04.15.
 */
public class TestDatabase extends AndroidTestCase {

    WeatherDbHelper helper;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        helper = new WeatherDbHelper(getContext());
    }

    public void _testDatabaseCanOpenWritableWhileOpenReadable() throws Exception {

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getId() + ": will open for reading");
                SQLiteDatabase readDb = helper.getReadableDatabase();
                System.out.println(Thread.currentThread().getId() + ": opened for reading");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                readDb.close();
                System.out.println(Thread.currentThread().getId() + ": closed for reading");
            }
        }).run();
//        Thread.sleep(1000);
        System.out.println(Thread.currentThread().getId() + ": will open for writing");
        SQLiteDatabase writeDb = helper.getWritableDatabase();
        System.out.println(Thread.currentThread().getId() + ": opened for writing");
        writeDb.close();
        System.out.println(Thread.currentThread().getId() + ": closed for writing");

        // TODO check that database can be opened for writing while there is open for reading and vice verse
    }

    public void testHelperIsNotNull() throws Exception {
        assertNotNull(helper);
    }

    public void testDatabaseExists() throws Exception {
        SQLiteDatabase db = helper.getReadableDatabase();
        assertNotNull(db);

        assertEquals(WeatherDbHelper.DATABASE_VERSION, db.getVersion());
    }

    public void testTablesExists() throws Exception {

    }
}

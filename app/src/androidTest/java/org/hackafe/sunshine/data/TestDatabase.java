package org.hackafe.sunshine.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.test.AndroidTestCase;
import android.util.Log;

import org.hackafe.sunshine.Forecast;

import java.util.Date;
import java.util.HashSet;

/**
 * Created by groupsky on 15.04.15.
 */
public class TestDatabase extends AndroidTestCase {

    WeatherDbHelper helper;
    SQLiteDatabase db;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        helper = new WeatherDbHelper(getContext());
        db = helper.getReadableDatabase();
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
        assertNotNull(db);

        assertEquals(WeatherDbHelper.DATABASE_VERSION, db.getVersion());
    }

    public void testSqlErrorProducesException() throws Exception {
        try {
            Cursor c = db.rawQuery("hackafe is the best", null);
            fail("sql garbage does't produce exception!");
        } catch (SQLiteException e) {

        }
    }

    public void testTablesExists() throws Exception {
        Cursor c = db.rawQuery("select name from sqlite_master where type = 'table' ", null);

        boolean found = false;
        assertTrue(c.moveToFirst());
        do {
            System.out.println("table = " + c.getString(0));
            if ("forecast".equals(c.getString(0)))
                found = true;
        } while (c.moveToNext());

        assertTrue("table forecast was not found!", found);
    }

    public void testForecastHasAllColumn() throws Exception {
        HashSet<String> expectedColumn = new HashSet<>();
        expectedColumn.add("_id");
        expectedColumn.add("forecast");
        expectedColumn.add("date");

        Cursor cursor = db.rawQuery("PRAGMA table_info(forecast)", null);
        int name_idx = cursor.getColumnIndex("name");

        assertTrue(cursor.moveToFirst());
        do {
            String columnName = cursor.getString(name_idx);
            expectedColumn.remove(columnName);
        } while (cursor.moveToNext());

        assertEquals(0, expectedColumn.size());
    }

    public void testSaveNewForecast() throws Exception {
        long timestamp = new Date().getTime();
        String forecastStr = "sunny all day long with chance for pizza";
        Forecast forecast = new Forecast(timestamp,
                forecastStr);
        helper.saveNewForecast(forecast);

        Cursor cursor = db.query(
                // table name
                "forecast",
                // select field
                new String[]{"forecast", "date"},
                // where clause
                "date = ?",
                // where argument
                new String[]{Long.toString(timestamp)},
                // group by
                null,
                // having
                null,
                // order by
                null
                );

        // check for single record
        assertEquals(1, cursor.getCount());
        // check we can move to the first record
        assertTrue(cursor.moveToFirst());
        // check forecast column == our forecast string
        assertEquals(forecastStr, cursor.getString(0));
        // check date column == our date
        assertEquals(timestamp, cursor.getLong(1));
    }
}

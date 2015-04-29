package org.hackafe.sunshine.data;

import static org.hackafe.sunshine.data.WeatherContract.*;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.test.AndroidTestCase;

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
        getContext().deleteDatabase(WeatherDbHelper.DATABASE_NAME);
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
        HashSet<String> tables = new HashSet<>();
        tables.add(Location.TABLE_NAME);
        tables.add(Forecast.TABLE_NAME);

        Cursor c = db.rawQuery("select name from sqlite_master where type = 'table' ", null);

        assertTrue(c.moveToFirst());
        do {
            System.out.println("table = " + c.getString(0));
            tables.remove(c.getString(0));
        } while (c.moveToNext());

        if (!tables.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (String table : tables)
                sb.append("missing table: " + table + "\n");
            fail("not all tables found!\n" + sb.toString());
        }
    }

    public void testForecastHasAllColumn() throws Exception {
        HashSet<String> expectedColumn = new HashSet<>();
        expectedColumn.add(Forecast._ID);
        expectedColumn.add(Forecast.COLUMN_DATE);
        expectedColumn.add(Forecast.COLUMN_FORECAST);

        Cursor cursor = db.rawQuery("PRAGMA table_info(" + Forecast.TABLE_NAME + ")", null);
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
        org.hackafe.sunshine.Forecast forecast = new org.hackafe.sunshine.Forecast(timestamp,
                forecastStr);
        helper.insertForecast(0, forecast);

        Cursor cursor = db.query(
                // table name
                Forecast.TABLE_NAME,
                // select field
                Forecast.PROJECTION,
                // where clause
                Forecast.COLUMN_DATE + " = ?",
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
        assertEquals(forecastStr, cursor.getString(Forecast.INDEX_FORECAST));
        // check date column == our date
        assertEquals(timestamp, cursor.getLong(Forecast.INDEX_DATE));
    }

    // TODO validate only one record per day (try to insert 2 for a single day)
    // TODO validate bad data
    // TODO validate we have a unique id for the inserted record

    public void testInsertLocation() {
        ContentValues values = new ContentValues();
        values.put(Location.COLUMN_NAME, "filibe");
        long id = helper.insertLocation(new ContentValues(values));
        assertEquals("id", 1, id);

        Cursor cursor = db.query(
                // table name
                Location.TABLE_NAME,
                // select field
                Location.PROJECTION,
                // where clause
                null,
                // where argument
                null,
                // group by
                null,
                // having
                null,
                // order by
                null
        );
        assertEquals(1, cursor.getCount());
        assertTrue(cursor.moveToFirst());
        assertEquals("id", id, cursor.getLong(Location.INDEX_ID));
        assertEquals("name", values.getAsString(Location.COLUMN_NAME), cursor.getString(Location.INDEX_NAME));
    }

    public void testForecastHasLocation() {
        ContentValues locationValues = new ContentValues();
        locationValues.put(Location.COLUMN_NAME, "puldin");
        long locationId = helper.insertLocation(locationValues);

        ContentValues values = new ContentValues();
        values.put(Forecast.COLUMN_LOCATION, locationId);
        values.put(Forecast.COLUMN_DATE, 0);
        values.put(Forecast.COLUMN_FORECAST, "");
        long forecastId = helper.insertForecast(values);

        assertTrue("expected forecast != -1, got " + forecastId, forecastId != -1);
    }

    public void testUpdateForecast() {
        ContentValues locationValues = new ContentValues();
        locationValues.put(Forecast.COLUMN_DATE, 1);
        locationValues.put(Forecast.COLUMN_LOCATION, 1);

        // old value
        locationValues.put(Forecast.COLUMN_FORECAST, "rainy day");
        helper.insertForecast(locationValues);

        // new value
        locationValues.put(Forecast.COLUMN_FORECAST, "sunny day");
        helper.insertForecast(locationValues);

        // check count
        Cursor cursor = db.rawQuery("select * from "+Forecast.TABLE_NAME, null);
        assertEquals(1, cursor.getCount());
    }
}


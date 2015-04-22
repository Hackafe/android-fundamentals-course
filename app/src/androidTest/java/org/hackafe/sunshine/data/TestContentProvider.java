package org.hackafe.sunshine.data;

import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;

import static org.hackafe.sunshine.data.WeatherContract.*;

import java.util.Date;

/**
 * Created by groupsky on 22.04.15.
 */
public class TestContentProvider extends AndroidTestCase {

    public void testWeHaveAProvider() {
        ContentProviderClient client = getContext().getContentResolver().acquireContentProviderClient(WeatherContract.Forecast.CONTENT_URI);
        assertNotNull("Can't find content provider", client);
        client.release();
    }

    public void testInsertData() {
        ContentValues values = new ContentValues();
        values.put(Forecast.COLUMN_DATE, new Date().getTime());
        values.put(Forecast.COLUMN_FORECAST, "some forecast for sunny days");
        values.put(Forecast.COLUMN_LOCATION, 1);
        Uri row = getContext().getContentResolver().insert(
                Forecast.CONTENT_URI,
                values
        );
        assertNotNull(row);

        Cursor cursor = new WeatherDbHelper(getContext()).getReadableDatabase().query(
                //String table,
                Forecast.TABLE_NAME,
                // String[] columns,
                null,
                // String selection,
                Forecast.COLUMN_DATE + "=?",
                // String[] selectionArgs,
                new String[]{values.getAsString(Forecast.COLUMN_DATE)},
                // String groupBy,
                null,
                // String having,
                null,
                // String orderBy
                null

        );

        assertEquals(1, cursor.getCount());
    }

    public void testQueryOneRecord() {
        org.hackafe.sunshine.Forecast forecast = new org.hackafe.sunshine.Forecast(new Date().getTime(), "event shinier day");
        new WeatherDbHelper(getContext()).insertForecast(1, forecast);

        Cursor cursor = getContext().getContentResolver().query(
                // Uri uri,
                Forecast.CONTENT_URI,
                // String[] projection,
                Forecast.PROJECTION,
                // String selection,
                Forecast.COLUMN_DATE + "=?",
                // String[] selectionArgs,
                new String[]{Long.toString(forecast.timestamp)},
                // String sortOrder
                null
        );

        assertEquals("records", 1, cursor.getCount());
        assertTrue("moveToFirst", cursor.moveToFirst());
        assertEquals("desc", forecast.desc, cursor.getString(Forecast.INDEX_FORECAST));
        assertEquals("timestamp", forecast.timestamp, cursor.getLong(Forecast.INDEX_DATE));
    }

    public void testInsertLocation() {
        ContentValues values = new ContentValues();
        values.put(Location.COLUMN_NAME, "pulpodeva");
        Uri uri = getContext().getContentResolver().insert(
                Location.CONTENT_URI,
                values
        );

        WeatherDbHelper helper = new WeatherDbHelper(getContext());
        SQLiteDatabase db = helper.getReadableDatabase();
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
        assertEquals("name", values.getAsString(Location.COLUMN_NAME), cursor.getString(Location.INDEX_NAME));

    }
}

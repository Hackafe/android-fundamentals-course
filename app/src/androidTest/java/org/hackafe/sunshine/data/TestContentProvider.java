package org.hackafe.sunshine.data;

import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;

import static org.hackafe.sunshine.data.WeatherContract.Forecast.*;

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
        values.put(WeatherContract.Forecast.COLUMN_DATE, new Date().getTime());
        values.put(WeatherContract.Forecast.COLUMN_FORECAST, "some forecast for sunny days");
        Uri row = getContext().getContentResolver().insert(
                WeatherContract.Forecast.CONTENT_URI,
                values
        );
        assertNotNull(row);

        Cursor cursor = new WeatherDbHelper(getContext()).getReadableDatabase().query(
                //String table,
                WeatherContract.Forecast.TABLE_NAME,
                // String[] columns,
                null,
                // String selection,
                WeatherContract.Forecast.COLUMN_DATE + "=?",
                // String[] selectionArgs,
                new String[]{values.getAsString(WeatherContract.Forecast.COLUMN_DATE)},
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
        new WeatherDbHelper(getContext()).insertForecast(forecast);

        Cursor cursor = getContext().getContentResolver().query(
                // Uri uri,
                CONTENT_URI,
                // String[] projection,
                PROJECTION,
                // String selection,
                COLUMN_DATE + "=?",
                // String[] selectionArgs,
                new String[]{Long.toString(forecast.timestamp)},
                // String sortOrder
                null
        );

        assertEquals("records", 1, cursor.getCount());
        assertTrue("moveToFirst", cursor.moveToFirst());
        assertEquals("desc", forecast.desc, cursor.getString(INDEX_FORECAST));
        assertEquals("timestamp", forecast.timestamp, cursor.getLong(INDEX_DATE));
    }

}

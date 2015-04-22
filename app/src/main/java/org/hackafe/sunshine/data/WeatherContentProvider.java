package org.hackafe.sunshine.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class WeatherContentProvider extends ContentProvider {
    public WeatherContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        WeatherDbHelper helper = new WeatherDbHelper(getContext());
        helper.insertForecast(values);
        return uri.buildUpon()
                .appendQueryParameter(
                        WeatherContract.Forecast.COLUMN_DATE,
                        values.getAsString(WeatherContract.Forecast.COLUMN_DATE))
                .build();
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        WeatherDbHelper helper = new WeatherDbHelper(getContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        return db.query(
                // String table,
                WeatherContract.Forecast.TABLE_NAME,
                // String[] columns,
                projection,
                // String selection,
                selection,
                // String[] selectionArgs,
                selectionArgs,
                // String groupBy,
                null,
                // String having,
                null,
                // String orderBy
                null);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

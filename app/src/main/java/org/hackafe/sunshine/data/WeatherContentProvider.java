package org.hackafe.sunshine.data;

import  static org.hackafe.sunshine.data.WeatherContract.*;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class WeatherContentProvider extends ContentProvider {

    private static final int LOCATION_CODE = 1;
    private static final int FORECAST_CODE = 2;
    static UriMatcher matcher = new UriMatcher(0);
    static {
        matcher.addURI(AUTHORITY, "location", LOCATION_CODE);
        matcher.addURI(AUTHORITY, "forecast", FORECAST_CODE);
    }

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
        switch (matcher.match(uri)) {
            case FORECAST_CODE:
                helper.insertForecast(values);
                getContext().getContentResolver().notifyChange(
                        Forecast.CONTENT_URI, null
                );
                return uri.buildUpon()
                        .appendQueryParameter(
                                WeatherContract.Forecast.COLUMN_DATE,
                                values.getAsString(WeatherContract.Forecast.COLUMN_DATE))
                        .build();
            case LOCATION_CODE:
                long id = helper.insertLocation(values);
                return uri.buildUpon()
                        .appendQueryParameter(
                                Location._ID,
                                Long.toString(id))
                        .build();
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
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
        Cursor cursor = db.query(
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
        cursor.setNotificationUri(getContext().getContentResolver(), Forecast.CONTENT_URI);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

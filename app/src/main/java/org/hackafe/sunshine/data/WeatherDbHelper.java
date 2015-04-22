package org.hackafe.sunshine.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static org.hackafe.sunshine.data.WeatherContract.*;

/**
 * Created by groupsky on 15.04.15.
 */
public class WeatherDbHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "weather.db";
    static final int DATABASE_VERSION = 7;

    public WeatherDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS forecast;");
        db.execSQL("DROP TABLE IF EXISTS forecasts;");
        db.execSQL("CREATE TABLE \"forecasts\" (\n" +
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "location_id INTEGER NOT NULL," +
                "weather TEXT NOT NULL," +
                "fordate INTEGER NOT NULL" +
                ");\n");

        db.execSQL("DROP TABLE IF EXISTS locations;");
        db.execSQL("CREATE TABLE \"locations\" (\n" +
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "name TEXT NOT NULL" +
                ");\n");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public void insertForecast(long location_id, org.hackafe.sunshine.Forecast forecast) {
        ContentValues values = new ContentValues();
        values.put(Forecast.COLUMN_FORECAST, forecast.desc);
        values.put(Forecast.COLUMN_DATE, forecast.timestamp);
        values.put(Forecast.COLUMN_LOCATION, location_id);
        insertForecast(values);
    }

    public long insertForecast(ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(Forecast.TABLE_NAME, null, values);
    }

    public long insertLocation(ContentValues values) {
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(Location.TABLE_NAME, null, values);
    }
}

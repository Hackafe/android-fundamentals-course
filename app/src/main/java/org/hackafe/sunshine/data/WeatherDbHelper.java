package org.hackafe.sunshine.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.hackafe.sunshine.Forecast;

/**
 * Created by groupsky on 15.04.15.
 */
public class WeatherDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "weather.db";
    static final int DATABASE_VERSION = 4;

    public WeatherDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS forecast;");
        db.execSQL("CREATE TABLE \"forecasts\" (\n" +
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "weather TEXT NOT NULL," +
                "fordate INTEGER NOT NULL" +
                ");\n");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public void saveNewForecast(Forecast forecast) {
        SQLiteDatabase db = getWritableDatabase();
//        try {
            ContentValues values = new ContentValues();
            values.put(WeatherContract.ForecastTable.COLUMN_FORECAST, forecast.desc);
            values.put(WeatherContract.ForecastTable.COLUMN_DATE, forecast.timestamp);
            db.insert(WeatherContract.ForecastTable.TABLE_NAME, null, values);
//        }finally {
//            db.close();
//        }
        // TODO find a way to work with multiple open databases from single thread!!!!!!!!!!!!!!
    }
}

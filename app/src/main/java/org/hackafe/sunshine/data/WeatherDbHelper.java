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
    static final int DATABASE_VERSION = 3;

    public WeatherDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS forecast;");
        db.execSQL("CREATE TABLE \"forecast\" (\n" +
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "forecast TEXT NOT NULL," +
                "date INTEGER NOT NULL" +
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
            values.put("forecast", forecast.desc);
            values.put("date", forecast.timestamp);
            db.insert("forecast", null, values);
//        }finally {
//            db.close();
//        }
        // TODO find a way to work with multiple open databases from single thread!!!!!!!!!!!!!!
    }
}

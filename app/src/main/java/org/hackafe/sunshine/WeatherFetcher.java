package org.hackafe.sunshine;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import org.hackafe.sunshine.data.WeatherContract;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
* Created by groupsky on 29.04.15.
*/
public class WeatherFetcher extends AsyncTask<String, Void, Void> {

    private ContentResolver contentResolver;

    public WeatherFetcher(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    private String getForecast(String location, String units) {
        try {
            URL url = new URL(String.format("http://api.openweathermap.org/data/2.5/forecast/daily?q=%s&mode=json&units=%s&cnt=14",
                    location,
                    units));
            InputStream inputStream = url.openStream();
            try {
                BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }

                return total.toString();
            } finally {
                inputStream.close();
            }
        } catch (Throwable t) {
            Log.e("Sunshine", t.getMessage(), t);
            return null;
        }
    }

    private void parseForecast(String data, String prefUnits) {
        try {
            if (TextUtils.isEmpty(data)) return;

            // parse String so we have JSONObject
            JSONObject obj = new JSONObject(data);
            // get "list" field as array
            JSONArray list = obj.getJSONArray("list");
            // iterate array and get forecast
            for (int i = 0; i < list.length(); i++) {
                // get "i"th forecast
                JSONObject forecastObj = list.getJSONObject(i);

                // get "temp" object
                JSONObject temp = forecastObj.getJSONObject("temp");
                // extract "day" temperature
                double dayTemp = temp.getDouble("day");

                // get "weather" array
                JSONArray weather = forecastObj.getJSONArray("weather");
                // get 1st weather
                JSONObject weather1 = weather.getJSONObject(0);

                // extract "description" from 1st weather
                String description = weather1.getString("description");

                // extract "dt" date time in unix epoch format
                long dt = forecastObj.getLong("dt");
                String dateStr = SimpleDateFormat.getDateInstance().format(new Date(dt * 1000));
                String degrees;

                if (prefUnits.equals("Metric"))
                    degrees = "C";
                else
                    degrees = "F";

                ContentValues forecast = new ContentValues();
                forecast.put(WeatherContract.Forecast.COLUMN_FORECAST, String.format("%s - %s   %.1f°%s", dateStr, description, dayTemp, degrees));
                forecast.put(WeatherContract.Forecast.COLUMN_DATE, dt);
                forecast.put(WeatherContract.Forecast.COLUMN_LOCATION, 1);
                insertForecast(forecast);
                Log.d("Sunshine", "forecast = " + forecast);
            }
        } catch (Throwable t) {
            Log.e("Sunshine", t.getMessage(), t);
        }
    }

    private void insertForecast(ContentValues forecast) {
        Uri row = contentResolver.insert(
                WeatherContract.Forecast.CONTENT_URI,
                forecast
        );
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Void doInBackground(String... params) {
        String location = params[0];
        String units = params[1];

        String data = getForecast(location, units);
        parseForecast(data, units);

        return null;
    }
}

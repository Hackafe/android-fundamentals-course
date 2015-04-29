package org.hackafe.sunshine;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.hackafe.sunshine.data.WeatherContract;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.hackafe.sunshine.data.WeatherContract.*;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {
    private static final String TAG = "ForecastFragment";
    SharedPreferences mSharedPreferences;
    String prefUnits;

    public ForecastFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        prefUnits = mSharedPreferences.getString("pref_units", "Metric");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy
                .Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String data = getForecast();
        List<Forecast> forecasts = parseForecast(data);

        for (Forecast forecast: forecasts) {
            ContentValues values = new ContentValues();
            values.put(WeatherContract.Forecast.COLUMN_DATE, forecast.timestamp);
            values.put(WeatherContract.Forecast.COLUMN_FORECAST, forecast.desc);
            values.put(WeatherContract.Forecast.COLUMN_LOCATION, 1);
            Uri row = getActivity().getContentResolver().insert(
                    WeatherContract.Forecast.CONTENT_URI,
                    values
            );
            Log.d(TAG, "added weather with uri: "+row);
        }


        final ForecastAdapter adapter = new ForecastAdapter(inflater, forecasts);
        final ListView collection = (ListView) rootView.findViewById(R.id.container);
        collection.setAdapter(adapter);

        collection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Forecast item = (Forecast) adapter.getItem(position);
                Intent intent = new Intent(getActivity(), DayForecast.class);
                intent.putExtra("TIMESTAMP", item.timestamp);
                intent.putExtra(Intent.EXTRA_TEXT, item.desc);
                startActivity(intent);

            }
        });

        final EditText countInput = (EditText) rootView.findViewById(R.id.countInput);


        Button addMoreBtn = (Button) rootView.findViewById(R.id.btn_add_more_items);


        return rootView;
    }

    private List<Forecast> parseForecast(String data) {
        try {
            List<Forecast> forecastList = new ArrayList<>();
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

                Forecast forecast = new Forecast();
                forecast.desc = String.format("%s - %s   %.1f°%s", dateStr, description, dayTemp, degrees);
                forecast.timestamp = dt;
                forecastList.add(forecast);
                Log.d("Sunshine", "forecast = " + forecast);
            }
            return forecastList;
        } catch (Throwable t) {
            Log.e("Sunshine", t.getMessage(), t);
            return null;
        }
    }

    private String getForecast() {
        try {
            URL url = new URL(String.format("http://api.openweathermap.org/data/2.5/forecast/daily?q=%s&mode=json&units=%s&cnt=7",
                    mSharedPreferences.getString("pref_location", "Plovdiv"),
                    prefUnits));
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
}

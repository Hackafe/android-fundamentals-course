package org.hackafe.sunshine;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import org.hackafe.sunshine.data.WeatherContentProvider;
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
public class ForecastFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "ForecastFragment";
    SharedPreferences mSharedPreferences;
    private SimpleCursorAdapter adapter;

    public ForecastFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        new WeatherFetcher(getActivity().getContentResolver()).execute(
                mSharedPreferences.getString("pref_location", "Plovdiv"),
                mSharedPreferences.getString("pref_units", "Metric")
        );

        adapter = new SimpleCursorAdapter(getActivity(),
                R.layout.list_item_forecast,
                null,
                new String[]{
                        WeatherContract.Forecast.COLUMN_FORECAST
                },
                new int[]{
                        R.id.list_item_forecast_listview
                },
                0);
        final ListView collection = (ListView) rootView.findViewById(R.id.container);
        collection.setAdapter(adapter);

        collection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) adapter.getItem(position);
                Intent intent = new Intent(getActivity(), DayForecast.class);
                intent.putExtra("TIMESTAMP", cursor.getLong(WeatherContract.Forecast.INDEX_DATE));
                intent.putExtra(Intent.EXTRA_TEXT, cursor.getString(WeatherContract.Forecast.INDEX_FORECAST));
                startActivity(intent);

            }
        });

        getLoaderManager().initLoader(1, null, this);

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                // Context context,
                getActivity(),
                // Uri uri,
                WeatherContract.Forecast.CONTENT_URI,
                // String[] projection,
                WeatherContract.Forecast.PROJECTION,
                // String selection,
                null,
                // String[] selectionArgs,
                null,
                // String sortOrder
                WeatherContract.Forecast.COLUMN_DATE
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    public static class WeatherFetcher extends AsyncTask<String, Void, Void> {

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

        private List<Forecast> parseForecast(String data, String prefUnits) {
            try {
                List<Forecast> forecastList = new ArrayList<>();

                if (TextUtils.isEmpty(data)) return forecastList;
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

        @Override
        protected Void doInBackground(String... params) {
            String location = params[0];
            String units = params[1];

            String data = getForecast(location, units);
            List<Forecast> forecasts = parseForecast(data, units);

            for (Forecast forecast : forecasts) {
                ContentValues values = new ContentValues();
                values.put(WeatherContract.Forecast.COLUMN_DATE, forecast.timestamp);
                values.put(WeatherContract.Forecast.COLUMN_FORECAST, forecast.desc);
                values.put(WeatherContract.Forecast.COLUMN_LOCATION, 1);
                Uri row = contentResolver.insert(
                        WeatherContract.Forecast.CONTENT_URI,
                        values
                );
                Log.d(TAG, "added weather with uri: " + row);
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }
    }
}

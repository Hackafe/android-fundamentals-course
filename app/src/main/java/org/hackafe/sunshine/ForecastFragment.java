package org.hackafe.sunshine;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import org.hackafe.sunshine.data.WeatherContract;

import de.greenrobot.event.EventBus;

import static org.hackafe.sunshine.data.WeatherContract.Forecast;
import static org.hackafe.sunshine.data.WeatherContract.Location;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,WeatherFetcher.Listener {
    private static final String TAG = "ForecastFragment";
    SharedPreferences mSharedPreferences;
    private SimpleCursorAdapter adapter;
    private long locationId;
    private View progressBar;
    private View loadingText;
    private View nodataText;
    private ListView collection;

    public ForecastFragment() {
    }

    private long getLocationId(String location) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(
                Location.CONTENT_URI,
                new String[]{Location._ID},
                Location.COLUMN_NAME + "=?",
                new String[]{location},
                null
        );
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getLong(0);
        }
        ContentValues values = new ContentValues();
        values.put(Location.COLUMN_NAME, location);
        contentResolver.insert(Location.CONTENT_URI, values);
        return getLocationId(location);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String location = mSharedPreferences.getString("pref_location", "Plovdiv");
        locationId = getLocationId(location);

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
        collection = (ListView) rootView.findViewById(R.id.container);
        final View emptyView = rootView.findViewById(R.id.empty);
        collection.setEmptyView(emptyView);
        collection.setAdapter(adapter);



        collection.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) adapter.getItem(position);

                long timestamp = cursor.getLong(Forecast.INDEX_DATE);
                String description = cursor.getString(Forecast.INDEX_FORECAST);
                ForecastItemSelectedEvent event = new ForecastItemSelectedEvent();
                event.timestamp = timestamp;
                event.description = description;
                EventBus.getDefault().post(event);
            }
        });

        progressBar = rootView.findViewById(R.id.progressBar);
        loadingText = rootView.findViewById(R.id.loading_text);
        nodataText = rootView.findViewById(R.id.nodata_text);

        getLoaderManager().initLoader(1, null, this);

        progressBar.setVisibility(View.VISIBLE);
        loadingText.setVisibility(View.VISIBLE);
        nodataText.setVisibility(View.GONE);

        AsyncTask<String, Void, Void> asyncTask = new WeatherFetcher(getActivity().getContentResolver(), this).execute(
                Long.toString(locationId),
                location,
                mSharedPreferences.getString("pref_units", "Metric")
        );

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                // Context context,
                getActivity(),
                // Uri uri,
                Forecast.CONTENT_URI,
                // String[] projection,
                Forecast.PROJECTION,
                // String selection,
                Forecast.COLUMN_LOCATION+"=?",
                // String[] selectionArgs,
                new String[]{ Long.toString(locationId) },
                // String sortOrder
                Forecast.COLUMN_DATE
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

    @Override
    public void onWeatherFetcherDone() {
        progressBar.setVisibility(View.GONE);
        loadingText.setVisibility(View.GONE);
        nodataText.setVisibility(View.VISIBLE);
    }
}

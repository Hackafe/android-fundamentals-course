package org.hackafe.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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

}

package org.hackafe.sunshine;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;


public class DayForecastFragment extends Fragment {
    private static final String ARG_FORECAST_TIMESTAMP = "forecast_timestamp";
    private static final String ARG_FORECAST_DETAILS = "forecast_details";

    private long timestamp;
    private String details;
    private TextView txtDayForecast;

    public static DayForecastFragment newInstance(long timestamp, String details) {
        DayForecastFragment fragment = new DayForecastFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_FORECAST_TIMESTAMP, timestamp);
        args.putString(ARG_FORECAST_DETAILS, details);
        fragment.setArguments(args);
        return fragment;
    }


    public DayForecastFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            timestamp = getArguments().getLong(ARG_FORECAST_TIMESTAMP);
            details = getArguments().getString(ARG_FORECAST_DETAILS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_day_forecast, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SimpleDateFormat date = new SimpleDateFormat();
        TextView txtForDate = (TextView) view.findViewById(R.id.txtForDate);
        txtDayForecast = (TextView) view.findViewById(R.id.txtDayForecast);


        String formatedForDay = getString(R.string.info_day_forecast_label,
                date.format(this.timestamp));
        txtForDate.setText(formatedForDay);
        txtDayForecast.setText(this.details);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_day_forecast, menu);



        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);
        // Fetch and store ShareActionProvider
        ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(getForecastShareIntent());
        }
    }

    private Intent getForecastShareIntent() {
        String shareHashTag = "#SunshineApp " + txtDayForecast.getText();

        //Create Intent
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareHashTag);
        return shareIntent;
    }
}

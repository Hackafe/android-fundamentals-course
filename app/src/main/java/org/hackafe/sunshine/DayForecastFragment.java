package org.hackafe.sunshine;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;


public class DayForecastFragment extends Fragment {
    private static final String ARG_FORECAST_TIMESTAMP = "forecast_timestamp";
    private static final String ARG_FORECAST_DETAILS = "forecast_details";

    private long timestamp;
    private String details;

    public static DayForecastFragment newInstance(long timestamp, String details) {
        DayForecastFragment fragment = new DayForecastFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_FORECAST_TIMESTAMP, timestamp);
        args.putString(ARG_FORECAST_DETAILS, details);
        fragment.setArguments(args);
        return fragment;
    }


    public DayForecastFragment() {
        // Required empty public constructor
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
        TextView txtDayForecast = (TextView) view.findViewById(R.id.txtDayForecast);


        String formatedForDay = getString(R.string.info_day_forecast_label,
                date.format(this.timestamp));
        txtForDate.setText(formatedForDay);
        txtDayForecast.setText(this.details);

    }
}

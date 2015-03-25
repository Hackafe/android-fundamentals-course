package org.hackafe.sunshine;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
* Created by groupsky on 11.03.15.
*/
public class ForecastAdapter extends BaseAdapter {


    private List<Forecast> daysForecastList;
    LayoutInflater inflater;

    public ForecastAdapter(LayoutInflater inflater, List<Forecast> daysForecastList) {
        // we need the inflater so we can create the row layout in getView()
        this.inflater = inflater;
        this.daysForecastList = daysForecastList;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rootRowView;
        if (convertView != null) {
            rootRowView = convertView;
        } else {
            rootRowView = inflater.inflate(R.layout.list_item_forecast, parent, false);
        }

        TextView label = (TextView) rootRowView.findViewById(R.id.list_item_forecast_listview);
        Forecast forecast = daysForecastList.get(position);
        label.setText(forecast.desc);

        return rootRowView;
    }

    @Override
    public int getCount() {
        return daysForecastList.size();
    }

    @Override
    public Object getItem(int position) {
        return daysForecastList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}

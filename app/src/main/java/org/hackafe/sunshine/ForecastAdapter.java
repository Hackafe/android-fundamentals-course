package org.hackafe.sunshine;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
* Created by groupsky on 11.03.15.
*/
public class ForecastAdapter extends BaseAdapter {

    LayoutInflater inflater;
    int rowCount;

    public ForecastAdapter(LayoutInflater inflater) {
        // we need the inflater so we can create the row layout in getView()
        this.inflater = inflater;
        rowCount = 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int layoutId = R.layout.list_item_forecast;
        // 4) use convertView to speedup scrolling
        int itemViewType = getItemViewType(position);

        if(itemViewType == 0) {
            layoutId = R.layout.list_item_forecast_dark;
        }


        View rootRowView;
        if (convertView != null) {
            rootRowView = convertView;
        } else {
            rootRowView = inflater.inflate(layoutId, parent, false);
        }

        TextView label = (TextView) rootRowView.findViewById(R.id.list_item_forecast_listview);
        label.setText("I'm row #" + position);

        return rootRowView;
    }

    @Override
    public int getCount() {
        // we have 100 000 rows
        return rowCount;
    }

    @Override
    public Object getItem(int position) {
        // our object is basicly the position
        return position;
    }

    @Override
    public long getItemId(int position) {
        // the position serves as itemId
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }

    public void addOneItem(Integer count) {
        rowCount += count;
        notifyDataSetChanged();
    }
}

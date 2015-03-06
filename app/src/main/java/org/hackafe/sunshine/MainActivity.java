package org.hackafe.sunshine;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            // 1) using only findViewById populate 4 rows
//            TextView row1 = (TextView)rootView.findViewById(R.id.row1);
//            TextView row2 = (TextView)rootView.findViewById(R.id.row2);
//            TextView row3 = (TextView)rootView.findViewById(R.id.row3);
//            TextView row4 = (TextView)rootView.findViewById(R.id.row4);
//            TextView row5 = (TextView)rootView.findViewById(R.id.row5);
//            TextView row6 = (TextView)rootView.findViewById(R.id.row6);
//
//            row1.setText("Sunday");
//            row2.setText("Monday");
//            row3.setText("Tuesday");
//            row4.setText("Wednesday");
//            row5.setText("Thursday");
//            row6.setText("Friday");

            // 2) using inflater.inflate and R.layout.list_item_forecast populate 10 000 rows
//            LinearLayout list = (LinearLayout) rootView.findViewById(R.id.container);
//            for (int i = 1; i <= 10000; i++) {
//                View rootRowView = inflater.inflate(R.layout.list_item_forecast, container, false);
//                TextView label = (TextView)rootRowView.findViewById(R.id.list_item_forecast_listview);
//                label.setText("I'm row #"+i);
//                list.addView(rootRowView);
//            }

            // 3) use ListView and display 100 000 rows
            BaseAdapter adapter = new ForecastAdapter(inflater);
            ListView collection = (ListView) rootView.findViewById(R.id.container);
            collection.setAdapter(adapter);

            return rootView;
        }
    }

    public static class ForecastAdapter extends BaseAdapter {

        LayoutInflater inflater;

        public ForecastAdapter(LayoutInflater inflater) {
            // we need the inflater so we can create the row layout in getView()
            this.inflater = inflater;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rootRowView = inflater.inflate(R.layout.list_item_forecast, parent, false);
            TextView label = (TextView) rootRowView.findViewById(R.id.list_item_forecast_listview);
            label.setText("I'm row #" + position);

            return rootRowView;
        }

        @Override
        public int getCount() {
            // we have 100 000 rows
            return 100000;
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
    }
}

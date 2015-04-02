package org.hackafe.sunshine;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;


public class DayForecast extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_forecast);

        Intent intent = getIntent();
        SimpleDateFormat date = new SimpleDateFormat("MMM dd yyyy", Locale.US);
        TextView txtForDate = (TextView) findViewById(R.id.txtForDate);
        TextView txtDayForecast = (TextView) findViewById(R.id.txtDayForecast);

        txtForDate.setText("Forecast for date " + date.format(intent.getLongExtra("TIMESTAMP", System.currentTimeMillis()) * 1000));
        txtDayForecast.setText(intent.getStringExtra(Intent.EXTRA_TEXT));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_day_forecast, menu);
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
}

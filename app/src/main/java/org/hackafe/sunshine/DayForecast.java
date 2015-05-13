package org.hackafe.sunshine;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;


public class DayForecast extends ActionBarActivity {

    Intent shareIntent;
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_forecast);



        Intent intent = getIntent();
        SimpleDateFormat date = new SimpleDateFormat();
        TextView txtForDate = (TextView) findViewById(R.id.txtForDate);
        TextView txtDayForecast = (TextView) findViewById(R.id.txtDayForecast);


        String formatedForDay = getString(R.string.info_day_forecast_label,
                date.format(
                        intent.getLongExtra("TIMESTAMP", System.currentTimeMillis()) * 1000));
        txtForDate.setText(formatedForDay);
        txtDayForecast.setText(intent.getStringExtra(Intent.EXTRA_TEXT));

        //Make a string from the HashTag and the weather forecast
        String shareHashTag = "#SunshineApp " + txtDayForecast.getText();

        //Create Intent
        shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareHashTag);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new
                ColorDrawable(Color.parseColor("#ffffff")));
        getSupportActionBar();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_day_forecast, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);
        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
        // Return true to display menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return (true);
            case R.id.action_settings: {
                return true;
            }
        }

            return super.onOptionsItemSelected(item);
        }
    }

package org.hackafe.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import de.greenrobot.event.EventBus;


public class MainActivity extends ActionBarActivity {
    SharedPreferences mSharedPreferences;
    String prefLocation, prefUnits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        prefLocation = mSharedPreferences.getString("pref_location", "");
        prefUnits = mSharedPreferences.getString("pref_units", "Metric");

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setBackgroundDrawable(new
                ColorDrawable(Color.parseColor("#ffffff")));

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new ForecastFragment())
                    .commit();
        }
    }

    public void onEvent(ForecastItemSelectedEvent event) {
        Log.d("MainActivity", "OnEvent --> ForecastItemSelectedEvent");
        Fragment detailsFragment = DayForecastFragment.newInstance(event.timestamp, event.description);
         getSupportFragmentManager().beginTransaction()
                 .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                 .replace(R.id.container, detailsFragment)
                 .addToBackStack(null).
                 commit();
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
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        if (id == R.id.action_location_map) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + mSharedPreferences.getString("pref_location", "Plovdiv").toUpperCase()));
            startActivity(intent);
            return true;
        }

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        prefLocation = mSharedPreferences.getString("pref_location", "");
        prefUnits = mSharedPreferences.getString("pref_units", "Metric");
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        if (!prefLocation.equals(mSharedPreferences.getString("pref_location", "")) ||
                !prefUnits.equals(mSharedPreferences.getString("pref_units", "Metric"))) {
            startActivity(new Intent(this, MainActivity.class));
            this.finish();
        }
    }


}

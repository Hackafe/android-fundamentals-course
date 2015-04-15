package org.hackafe.sunshine.data;

import android.provider.BaseColumns;

/**
 * Created by groupsky on 15.04.15.
 */
public interface WeatherContract {

    public static interface ForecastTable extends BaseColumns {

        public static final String TABLE_NAME = "forecasts";

        public static final String COLUMN_FORECAST = "weather";
        public static final String COLUMN_DATE = "fordate";

        String[] PROJECTION = {
                _ID, COLUMN_DATE, COLUMN_FORECAST
        };

        public static final int INDEX_ID = 0;
        public static final int INDEX_DATE = 1;
        public static final int INDEX_FORECAST = 2;
    }

}

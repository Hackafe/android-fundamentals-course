package org.hackafe.sunshine.data;

import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;

/**
 * Created by groupsky on 15.04.15.
 */
public interface WeatherContract {
    /**
     * The authority for the contacts provider
     */
    public static final String AUTHORITY = "org.hackafe.sunshine";
    /**
     * A content:// style uri to the authority for the contacts provider
     */
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    public static interface ForecastTable extends BaseColumns {

        /**
         * The content:// style URI for this table.  Requests to this URI can be
         * performed on the UI thread because they are always unblocking.
         */
        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(AUTHORITY_URI, "forecast");

        public static final String TABLE_NAME = "forecasts";

        /**
         * type string
         */
        public static final String COLUMN_FORECAST = "weather";
        /**
         * type long
         */
        public static final String COLUMN_DATE = "fordate";

        String[] PROJECTION = {
                _ID, COLUMN_DATE, COLUMN_FORECAST
        };

        public static final int INDEX_ID = 0;
        public static final int INDEX_DATE = 1;
        public static final int INDEX_FORECAST = 2;
    }

}

package org.hackafe.sunshine.data;

import android.content.ContentProviderClient;
import android.test.AndroidTestCase;

/**
 * Created by groupsky on 22.04.15.
 */
public class TestContentProvider extends AndroidTestCase {

    public void testWeHaveAProvider() {
        ContentProviderClient client = getContext().getContentResolver().acquireContentProviderClient(WeatherContract.ForecastTable.CONTENT_URI);
        assertNotNull("Can't find content provider", client);
        client.release();
    }

}

package org.hackafe.sunshine;

/**
 * Created by groupsky on 25.03.15.
 */
public class Forecast {
    public long timestamp;
    public String desc;

    public Forecast() {
    }

    public Forecast(long timestamp, String desc) {
        this.timestamp = timestamp;
        this.desc = desc;
    }
}

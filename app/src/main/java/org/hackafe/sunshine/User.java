package org.hackafe.sunshine;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ironsteel on 3/18/15.
 */
public class User {
    public String firstName;
    public String lastName;
    public String facebook;

    public User(String firstName, String lastName, String facebook) {
        this.facebook = facebook;
        this.firstName = firstName;
        this.lastName = lastName;
    }


    public static User createFromJson(JSONObject jsonUser) throws JSONException {

        String firstName = jsonUser.getString("name1");

        String lastName = jsonUser.getString("name2");

        String facebookId = jsonUser.getString("facebook");
        return new User(firstName, lastName, facebookId);
    }
}

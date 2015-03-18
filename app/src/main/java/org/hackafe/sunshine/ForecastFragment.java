package org.hackafe.sunshine;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {

    public ForecastFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy
                .Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String data = getPresentUsersInTheLab();
        WhoIsInTheLabData whoIsInTheLabData = parseUserData(data);

        TextView usersCountView = (TextView) rootView.findViewById(R.id.users_count);
        usersCountView.setText(String.format("Users %d Guests %d", whoIsInTheLabData.totalUsersCount, whoIsInTheLabData.guestUsersCount));



        final UsersAdapter adapter = new UsersAdapter(inflater, whoIsInTheLabData.users);
        final ListView collection = (ListView) rootView.findViewById(R.id.container);
        collection.setAdapter(adapter);




        return rootView;
    }

    private WhoIsInTheLabData parseUserData(String data) {
        try {
            WhoIsInTheLabData whoIsInTheLabData = new WhoIsInTheLabData();
            List<User> usersList = new ArrayList<User>();
            // parse String so we have JSONObject
            JSONObject labData = new JSONObject(data).getJSONObject("data");
            // get "list" field as array
            JSONArray jsonUsers = labData.getJSONArray("users");
            // iterate array and get forecast
            for (int i=0; i<jsonUsers.length(); i++) {
                // get "i"th forecast
                JSONObject jsonUser = jsonUsers.getJSONObject(i);
                User user = User.createFromJson(jsonUser);
                usersList.add(user);

                Log.d("Sunshine", "user = "+user.firstName);
            }
            whoIsInTheLabData.users = usersList;
            whoIsInTheLabData.totalUsersCount = labData.getInt("count");
            whoIsInTheLabData.guestUsersCount = labData.getInt("guests");
            return whoIsInTheLabData;
        } catch (Throwable t) {
            Log.e("Sunshine", t.getMessage(), t);
            return null;
        }
    }

    private String getPresentUsersInTheLab() {
        try {
            URL url = new URL("http://78.130.204.197/?format=json");
            InputStream inputStream = url.openStream();
            try {
                BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }

                return total.toString();
            } finally {
                inputStream.close();
            }
        } catch (Throwable t) {
            Log.e("Sunshine", t.getMessage(), t);
            return null;
        }
    }
}

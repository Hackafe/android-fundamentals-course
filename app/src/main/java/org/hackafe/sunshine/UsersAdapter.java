package org.hackafe.sunshine;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
* Created by groupsky on 11.03.15.
*/
public class UsersAdapter extends BaseAdapter {


    private List<User> users;
    LayoutInflater inflater;

    public UsersAdapter(LayoutInflater inflater, List<User> users) {
        // we need the inflater so we can create the row layout in getView()
        this.inflater = inflater;
        this.users = users;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rootRowView;
        if (convertView != null) {
            rootRowView = convertView;
        } else {
            rootRowView = inflater.inflate(R.layout.list_item_forecast, parent, false);
        }

        TextView nameTv = (TextView) rootRowView.findViewById(R.id.user_name);
        TextView facebookTv = (TextView) rootRowView.findViewById(R.id.user_facebook);

        User user = users.get(position);
        nameTv.setText(user.firstName + " " + user.lastName);
        facebookTv.setText(String.format("Facebook: %s",user.facebook));


        return rootRowView;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}

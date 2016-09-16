package com.example.alan_flores.sunshine;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by alan_flores on 9/15/16.
 */
public class Adapter extends ArrayAdapter<Item> {

    public Adapter(Activity context, ArrayList<Item> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_forecast, parent, false);
        }

        Item currentItem = getItem(position);

        TextView text = (TextView) listItemView.findViewById(R.id.list_item_forecast);
        text.setText(currentItem.getText());

        return listItemView;
    }
}

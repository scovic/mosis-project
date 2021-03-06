package com.mosis.treasurehunt.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mosis.treasurehunt.R;
import com.mosis.treasurehunt.models.Hunt;

import java.util.ArrayList;
import java.util.List;

public class HuntAdapter extends ArrayAdapter<Hunt> {
    public enum  FilterType {
        COMPLETED,
        ACTIVE,
        MINE
    }

    private FilterType mFilter;
    private Context mContext;
    private List<Hunt> huntList;

    public HuntAdapter(@NonNull Context context, List<Hunt> list) {
        super(context, 0, list);
        mContext = context;
        huntList = list;

    }

    public void setmFilter(FilterType filter) { this.mFilter = filter; }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        Hunt currentHunt = huntList.get(position);
        StringBuilder stringBuilder = new StringBuilder();

        if(this.mFilter == FilterType.COMPLETED) {
            if(listItem == null)
                listItem = LayoutInflater.from(mContext).inflate(R.layout.item_list_hunt, parent, false);

            TextView hunt_name = listItem.findViewById(R.id.lbl_item_hunt_name);
            hunt_name.setText(currentHunt.getTitle());

            TextView hunt_points = listItem.findViewById(R.id.lbl_item_hunt_details);
            stringBuilder.append("");
            stringBuilder.append(currentHunt.getPoints());
            hunt_points.setText(stringBuilder.toString());
        } else if (this.mFilter == FilterType.ACTIVE) {
            if(listItem == null)
                listItem = LayoutInflater.from(mContext).inflate(R.layout.item_list_hunt, parent, false);

            TextView hunt_name = listItem.findViewById(R.id.lbl_item_hunt_name);
            hunt_name.setText(currentHunt.getTitle());

            TextView hunt_points = listItem.findViewById(R.id.lbl_item_hunt_details);
            stringBuilder.append("");
            stringBuilder.append(currentHunt.getNumberOfHunters());
            hunt_points.setText(stringBuilder.toString());
        } else {
            if(listItem == null)
                listItem = LayoutInflater.from(mContext).inflate(R.layout.item_list_hunt, parent, false);

            TextView hunt_name = listItem.findViewById(R.id.lbl_item_hunt_name);
            hunt_name.setText(currentHunt.getTitle());

            TextView hunt_points = listItem.findViewById(R.id.lbl_item_hunt_details);
            stringBuilder.append("");
            stringBuilder.append(currentHunt.getNumberOfClues());
            hunt_points.setText(stringBuilder.toString());
        }


        return listItem;

    }



}

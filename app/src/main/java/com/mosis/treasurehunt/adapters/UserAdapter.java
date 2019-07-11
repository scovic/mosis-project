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
import com.mosis.treasurehunt.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {
    private Context mContext;
    private List<User> userList = new ArrayList<>();

    public UserAdapter(@NonNull Context context, List<User> list) {
        super(context, 0, list);
        mContext = context;
        userList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        View listItem = convertView;
        User currentUser = userList.get(position);

        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_list_leaderboard, parent, false);

        TextView user_name = listItem.findViewById(R.id.text_user_name);
        user_name.setText(currentUser.getFullName());

        TextView points = listItem.findViewById(R.id.text_points);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Points: ");
        stringBuilder.append(currentUser.getPoints());
        points.setText(stringBuilder.toString());

        TextView rank = listItem.findViewById(R.id.text_rank);
        stringBuilder = new StringBuilder();
        stringBuilder.append(position+1);
        rank.setText(stringBuilder.toString());

        return listItem;
    }
}
package com.mosis.treasurehunt.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mosis.treasurehunt.R;
import com.mosis.treasurehunt.models.Feed;

import java.util.ArrayList;
import java.util.List;

public class FeedAdapter extends ArrayAdapter<Feed> {
    private Context mContext;
    private List<Feed> feedsList = new ArrayList<>();

    public FeedAdapter(@NonNull Context context, ArrayList<Feed> list) {
        super(context, 0, list);
        mContext = context;
        feedsList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_list_feeds, parent, false);

        Feed currentFeed = feedsList.get(position);

//        ImageView avatar_pic = (ImageView) listItem.findViewById(R.id.image_avatar);
//        avatar_pic.setImageResource(currentFeed.getOwner().profileImage);

        TextView user_name = (TextView) listItem.findViewById(R.id.text_user_name);
        user_name.setText(currentFeed.getOwner().getFullName());

        TextView info = (TextView) listItem.findViewById(R.id.text_info);
        info.setText("successful completed the hunt");

        return listItem;
    }
}

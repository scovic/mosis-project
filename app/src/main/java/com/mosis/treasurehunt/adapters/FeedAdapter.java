package com.mosis.treasurehunt.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mosis.treasurehunt.R;
import com.mosis.treasurehunt.models.Feed;

import java.util.ArrayList;
import java.util.List;

public class FeedAdapter extends ArrayAdapter<Feed> {
    private Context mContext;
    private List<Feed> feedsList = new ArrayList<>();

    public FeedAdapter(@NonNull Context context, List<Feed> list) {
        super(context, 0, list);
        mContext = context;
        feedsList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        Feed currentFeed = feedsList.get(position);

        if (currentFeed.getType() == Feed.Type.FINISH) {
            if (listItem == null)
                listItem = LayoutInflater.from(mContext).inflate(R.layout.item_list_finish_feed, parent, false);

//        ImageView avatar_pic = (ImageView) listItem.findViewById(R.id.image_avatar);
//        avatar_pic.setImageResource(currentFeed.getOwner().profileImage);

            TextView user_name = listItem.findViewById(R.id.text_user_name);
            user_name.setText(currentFeed.getOwner().getFullName());

            TextView info = listItem.findViewById(R.id.text_info);
            info.setText("successfully completed the hunt");
        } else {
            if (listItem == null)
                listItem = LayoutInflater.from(mContext).inflate(R.layout.item_list_create_feed, parent, false);

//        ImageView avatar_pic = (ImageView) listItem.findViewById(R.id.image_avatar);
//        avatar_pic.setImageResource(currentFeed.getOwner().profileImage);

            TextView user_name = listItem.findViewById(R.id.text_user_name);
            user_name.setText(currentFeed.getOwner().getFullName());

            TextView info = listItem.findViewById(R.id.text_info);
            info.setText("created a new hunt");

            TextView huntTitle = listItem.findViewById(R.id.text_hunt_title);
            huntTitle.setText(currentFeed.getHunt().getTitle());

            TextView numOfClues = listItem.findViewById(R.id.text_num_of_clues);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("");
            stringBuilder.append(currentFeed.getHunt().getNumberOfClues());
            stringBuilder.append(" clues");

            numOfClues.setText(stringBuilder.toString());
        }

        return listItem;
    }
}

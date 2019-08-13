package com.mosis.treasurehunt.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mosis.treasurehunt.R;
import com.mosis.treasurehunt.models.Clue;
import com.mosis.treasurehunt.models.Hunt;

import java.util.ArrayList;
import java.util.List;

public class ClueAdapter extends ArrayAdapter<Clue> {
    private Context mContext;
    private List<Clue> mClueList = new ArrayList<>();
    private Hunt mHunt;

    public ClueAdapter(@NonNull Context context, List<Clue> clueList, Hunt hunt) {
        super(context, 0, clueList);
        mContext = context;
        mClueList = clueList;
        mHunt = hunt;
    }

    public void updateContet(ArrayList<Clue> newList) {
        this.mClueList = newList;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        final Clue clue = mClueList.get(position);

        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.item_list_clue, parent, false);
        }

        TextView clueTitle = listItem.findViewById(R.id.lbl_item_clue_name);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Clue #");
        stringBuilder.append(position);
        clueTitle.setText(stringBuilder.toString());

        ImageView delete_icon = listItem.findViewById(R.id.img_delete);
        delete_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHunt.deleteClue(clue);
                updateContet(mHunt.getClues());
            }
        });

        return  listItem;
    }
}

package com.mosis.treasurehunt.data;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mosis.treasurehunt.models.Feed;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class FeedDao implements Dao<Feed> {

    private ArrayList<Feed> mFeeds;
    private DatabaseReference mDatabase;
    private final String COLLECTION = "feeds";

    public FeedDao() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        this.mFeeds = new ArrayList<>();
    }

    ListUpdatedEventListener updateListener;
    public void setEventListener(ListUpdatedEventListener listener) {
        updateListener = listener;
    }

    public interface ListUpdatedEventListener {
        void onListUpdated();
    }

    ValueEventListener feedListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot feedSnapshot : dataSnapshot.getChildren()) {
                Feed feed = feedSnapshot.getValue(Feed.class);
                mFeeds.add(feed);
            }

            if (updateListener != null) updateListener.onListUpdated();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            final String TAG = "FeedDao";
            Log.w(TAG, "feedListener:onCancelled", databaseError.toException());
        }
    };

    @Override
    public ArrayList<Feed> getAll() {
        return this.mFeeds;
    }

    @Override
    public void save(Feed feed) {
        this.mDatabase.child(COLLECTION).setValue(feed);

        if (updateListener != null) updateListener.onListUpdated();
    }

    @Override
    public void update(Feed feed) {
        this.mDatabase.child(COLLECTION).child(feed.getKey()).setValue(feed);

        if (updateListener != null) updateListener.onListUpdated();
    }

    @Override
    public void delete(Feed feed) {
        this.mDatabase.child(COLLECTION).child(feed.getKey()).removeValue();

        if (updateListener != null) updateListener.onListUpdated();
    }
}


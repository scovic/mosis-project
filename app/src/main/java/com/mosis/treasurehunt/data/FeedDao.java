package com.mosis.treasurehunt.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
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
    private static final String COLLECTION = "feeds";
    ListUpdatedEventListener updateListener;

    public void setEventListener(ListUpdatedEventListener listener) {
        updateListener = listener;
    }

    public interface ListUpdatedEventListener {
        void onListUpdated();
    }

    public FeedDao() {
        this.mFeeds = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(COLLECTION).addChildEventListener(childEventListener);
        mDatabase.child(COLLECTION).addValueEventListener(feedListener);
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

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Feed feed = dataSnapshot.getValue(Feed.class);
            mFeeds.add(feed);
            if (updateListener != null)
                updateListener.onListUpdated();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            Feed feed = dataSnapshot.getValue(Feed.class);
            mFeeds.remove(feed);

            if (updateListener != null)
                updateListener.onListUpdated();
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    @Override
    public ArrayList<Feed> getAll() {
        return mFeeds;
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


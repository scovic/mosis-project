package com.mosis.treasurehunt.data;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mosis.treasurehunt.models.Hunt;

import java.util.ArrayList;

public class HuntDao implements Dao<Hunt> {

    private ArrayList<Hunt> mHunts;
    private DatabaseReference mDatabase;
    private final String COLLECTION = "hunts";

    public HuntDao() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        this.mHunts = new ArrayList<>();
    }

    ListUpdatedEventListener updateListener;
    public void setEventListener(ListUpdatedEventListener listener) {
        updateListener = listener;
    }

    public interface ListUpdatedEventListener {
        void onListUpdated();
    }

    ValueEventListener huntListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for(DataSnapshot huntSnapshot : dataSnapshot.getChildren()) {
                Hunt hunt = huntSnapshot.getValue(Hunt.class);
                mHunts.add(hunt);
            }

            if (updateListener != null) updateListener.onListUpdated();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            final String TAG = "HuntDao";
            Log.w(TAG, "huntListener:onCancelled", databaseError.toException());
        }
    };

    @Override
    public ArrayList<Hunt> getAll() {
        return this.mHunts;
    }

    @Override
    public void save(Hunt hunt) {
        this.mDatabase.child(COLLECTION).setValue(hunt);

        if (updateListener != null) updateListener.onListUpdated();
    }

    @Override
    public void update(Hunt hunt) {
        this.mDatabase.child(COLLECTION).child(hunt.getKey()).setValue(hunt);

        if (updateListener != null) updateListener.onListUpdated();
    }

    @Override
    public void delete(Hunt hunt) {
        this.mDatabase.child(COLLECTION).child(hunt.getKey()).removeValue();

        if (updateListener != null) updateListener.onListUpdated();
    }
}

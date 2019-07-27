package com.mosis.treasurehunt.data;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mosis.treasurehunt.models.User;

import java.util.ArrayList;

public class UserDao implements Dao<User> {

    private ArrayList<User> mUsers;
    private DatabaseReference mDatabase;
    private final String COLLECTION = "users";

    public UserDao() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        this.mUsers = new ArrayList<>();
    }

    ListUpdatedEventListener updateListener;
    public void setEventListener(ListUpdatedEventListener listener) {
        updateListener = listener;
    }

    public interface ListUpdatedEventListener {
        void onListUpdated();
    }

    ValueEventListener userListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for(DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                User user = userSnapshot.getValue(User.class);
                mUsers.add(user);
            }

            if (updateListener != null) updateListener.onListUpdated();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            final String TAG = "UserDao";
            Log.w(TAG, "userListener:onCancelled", databaseError.toException());
        }
    };

    @Override
    public ArrayList<User> getAll() {
        return this.mUsers;
    }

    @Override
    public void save(User user) {
        this.mDatabase.child(COLLECTION).setValue(user);

        if (updateListener != null) updateListener.onListUpdated();
    }

    @Override
    public void update(User user) { // pod pretpostavkom da je key neka vrsta id
        this.mDatabase.child(COLLECTION).child(user.getKey()).setValue(user);

        if (updateListener != null) updateListener.onListUpdated();
    }

    @Override
    public void delete(User user) {
        this.mDatabase.child(COLLECTION).child(user.getKey()).removeValue();

        if (updateListener != null) updateListener.onListUpdated();
    }
}

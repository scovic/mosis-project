package com.mosis.treasurehunt.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
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
    private static final String COLLECTION = "users";
    private boolean QUERY_SUCCESS;

    public UserDao() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(COLLECTION).addChildEventListener(childEventListener);
        mDatabase.child(COLLECTION).addValueEventListener(userListener);
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

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            User user = dataSnapshot.getValue(User.class);
            mUsers.add(user);
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            User user = dataSnapshot.getValue(User.class);
            mUsers.remove(user);
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    @Override
    public ArrayList<User> getAll() {
        return this.mUsers;
    }

    @Override
    public void save(User user) {
        this.mDatabase.child(COLLECTION).child(user.getUsername()).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        setQuerySuccess(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        setQuerySuccess(false);
                    }
                });

        if (updateListener != null) updateListener.onListUpdated();
    }

    @Override
    public void update(User user) {
        this.mDatabase.child(COLLECTION).child(user.getUsername()).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        QUERY_SUCCESS = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        QUERY_SUCCESS = false;
                    }
                });

        if (updateListener != null) updateListener.onListUpdated();
    }

    @Override
    public void delete(User user) {
        this.mDatabase.child(COLLECTION).child(user.getUsername()).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        QUERY_SUCCESS = true;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        QUERY_SUCCESS = false;
                    }
                });

        if (updateListener != null) updateListener.onListUpdated();
    }

    public void setQuerySuccess(boolean b) {
        this.QUERY_SUCCESS = b;
    }

    public boolean getQuerySuccess() {
        return this.QUERY_SUCCESS;
    }

}

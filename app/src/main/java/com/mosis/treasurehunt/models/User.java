package com.mosis.treasurehunt.models;

import android.media.Image;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String firstName;
    public String lastName;
    public String mEmail;
    private int mPoints;
    @Exclude
    private String mKey;
    @Exclude
    public Image profileImage;

    public User() {}

    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mEmail = email;
        this.mPoints = 0;
    }

    public User(String firstName, String lastName, String email, int points) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mEmail = email;
        this.mPoints = points;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getFullName() { return String.format("%s %s", firstName, lastName); }

    public void setPoints(int points) { this.mPoints = points; }

    public int getPoints() { return this.mPoints; }

    public String getKey() { return this.mKey; }

    public void setKey(String key) { this.mKey = key; }

    @Override
    public String toString() { return String.format("%s %s", firstName, lastName); }
}

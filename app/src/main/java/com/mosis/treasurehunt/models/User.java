package com.mosis.treasurehunt.models;

import android.media.Image;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    private String firstName;
    private String lastName;
    private String mUsername;
    private String mPassword;
    private int mPoints;
    @Exclude
    private String mKey;
    @Exclude
    public Image profileImage;


    public User() {}

    public User(String firstName, String lastName, String username, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mUsername = username;
        this.mPassword = password;
        this.mPoints = 0;
    }

    public User(String firstName, String lastName, String username, String password, int points) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mUsername = username;
        this.mPassword = password;
        this.mPoints = points;
    }

    public User(String firstName, String lastName, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mUsername = username;
        this.mPoints = 0;
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

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        this.mUsername = username;
    }

    public String getFullName() { return String.format("%s %s", firstName, lastName); }

    public void setPoints(int points) { this.mPoints = points; }

    public int getPoints() { return this.mPoints; }

    public void setPassword(String password) {
        this.mPassword = password;
    }

    public String getPassword() { return this.mPassword; }

    public String getKey() { return this.mKey; }

    public void setKey(String key) { this.mKey = key; }

    @Override
    public String toString() { return String.format("%s %s", firstName, lastName); }

}

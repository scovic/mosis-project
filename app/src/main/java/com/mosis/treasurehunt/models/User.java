package com.mosis.treasurehunt.models;

import android.media.Image;

public class User {
    public String firstName;
    public String lastName;
    public String mEmail;
    public Image profileImage;
    private int mPoints;

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

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getFullName() { return String.format("%s %s", firstName, lastName); }

    public void setPoints(int points) { this.mPoints = points; }

    public int getPoints() { return this.mPoints; }
}

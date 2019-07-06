package com.mosis.treasurehunt.models;

import android.media.Image;

public class User {
    public String firstName;
    public String lastName;
    public String mEmail;
    public Image profileImage;

    public User(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mEmail = email;
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
}

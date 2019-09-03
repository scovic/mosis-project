package com.mosis.treasurehunt.models;

import com.google.firebase.database.Exclude;

public class Feed {
    public enum Type {
        CREATE,
        FINISH
    }

    private User mOwner;
    private Hunt mHunt;
    private Type mType;
    @Exclude
    private String mKey;

    public Feed() {}

    public Feed (User user, Hunt hunt, Type type) {
        this.mOwner = user;
        this.mHunt = hunt;
        this.mType = type;
    }

    public User getOwner() {
        return this.mOwner;
    }

    public Hunt getHunt() {
        return this.mHunt;
    }

    public Type getType() {
        return  this.mType;
    }

    public String getText() {
        return mType == Type.CREATE
                ? "created a new hunt"
                : "successfully completed the hunt";
    }

    public String getKey() { return this.mKey; }

    public void setKey(String key) { this.mKey = key; }
}

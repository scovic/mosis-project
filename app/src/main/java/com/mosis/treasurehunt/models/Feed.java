package com.mosis.treasurehunt.models;



public class Feed {
    public enum Type {
        CREATE,
        FINISH
    }

    private User mOwner;
    private Hunt mHunt;
    private Type mType;

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
                : "successfuly completed the hunt";
    }
}

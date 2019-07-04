package com.mosis.treasurehunt.models;

enum Type {
    CREATE,
    FINISH
}

public class Feed {
    private User mOwner;
    private Hunt mHunt;
    private Type mType;

    public Feed (User user, Hunt hunt, Type type) {
        this.mOwner = user;
        this.mHunt = hunt;
        this.mType = type;
    }
}

package com.mosis.treasurehunt.models;

import java.util.ArrayList;

public class Hunt {
    private String mTitle;
    private boolean mCompleted;
    private User mOwner;
    public ArrayList<Clue> clues;

    public Hunt() {
        clues = new ArrayList<>();
    }

    public Hunt(String title) {
        mTitle = title;
        clues = new ArrayList<>();
    }

    public Hunt(String title, User owner) {
        mTitle = title;
        mOwner = owner;
        clues = new ArrayList<>();
    }

    public void setTitle(String title) { this.mTitle = title;  }

    public String getTitle() { return this.mTitle; }

    public int getNumberOfClues() { return this.clues.size(); }

    public boolean checkCompleted() { return this.mCompleted; }

    public void setmCompleted() { this.mCompleted = true; }

    public User getmOwner() { return this.mOwner; }

    public void setmOwner(User owner) { this.mOwner = owner; }
}

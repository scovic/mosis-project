package com.mosis.treasurehunt.models;

import java.util.ArrayList;

public class Hunt {
    private String mTitle;
    public ArrayList<Clue> clues;

    public Hunt() {
        clues = new ArrayList<>();
    }

    public Hunt(String title) {
        mTitle = title;
        clues = new ArrayList<>();
    }

    public void setTitle(String title) { this.mTitle = title;  }

    public String getTitle() { return this.mTitle; }

    public int getNumberOfClues() { return this.clues.size(); }
}

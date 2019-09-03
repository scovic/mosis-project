package com.mosis.treasurehunt.models;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;

public class Hunt {
    private String title;
    private boolean completed;
    private int points;
    private User owner;
    public ArrayList<Clue> clues;
    public ArrayList<User> hunters;
    @Exclude
    private String key;

    public Hunt() {
        clues = new ArrayList<>();
        hunters = new ArrayList<>();
    }

    public Hunt(String title) {
        this.title = title;
        clues = new ArrayList<>();
        hunters = new ArrayList<>();
    }

    public Hunt(String title, User owner) {
        this.title = title;
        this.owner = owner;
        clues = new ArrayList<>();
        hunters = new ArrayList<>();
    }

    public void setTitle(String title) { this.title = title;  }

    public String getTitle() { return this.title; }

    public boolean checkCompleted() { return this.completed; }

    public void setmCompleted() { this.completed = true; }

    public User getOwner() { return this.owner; }

    public String getOwnerUsername() {
        return this.owner.getUsername();
    }

    public void setOwner(User owner) { this.owner = owner; }

    public int getPoints() { return this.points; }

    public void setPoints(int pts) { this.points = pts; }

    public int getNumberOfClues() { return this.clues.size(); }

    public int getNumberOfHunters() { return this.hunters.size(); }

    public String getKey() { return this.key; }

    public void setKey(String key) { this.key = key; }

    public void addClue(Clue clue) { this.clues.add(clue); }

    public ArrayList<Clue> getClues() { return this.clues; }

    public void deleteClue(Clue clue) {
        this.clues.remove(clue);
    }

    // Finds first unanswered clue
    public Clue getUnansweredClue() {
        Clue clue = null;
        for (Clue c : this.clues) {
            if (c.isAnswered()) {
                clue = c;
                break;
            }
        }
        return clue;
    }

    public boolean isCompleted() {
        boolean isCompleted = true;
        for(Clue clue: this.clues) {
            if (!clue.isAnswered()) {
                isCompleted = false;
                break;
            }
        }

        return  isCompleted;
    }

}

package com.mosis.treasurehunt.models;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;

public class Hunt {
    private String title;
    private boolean completed;
    private int points;
    private String owner;
    public ArrayList<Clue> clues;
    public ArrayList<String> hunters;
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

    public Hunt(String title, String owner) {
        this.title = title;
        this.owner = owner;
        clues = new ArrayList<>();
        hunters = new ArrayList<>();
    }

    public void setTitle(String title) { this.title = title;  }

    public String getTitle() { return this.title; }

    public boolean checkCompleted() { return this.completed; }

    public void setCompleted() { this.completed = true; }

    public String getOwner() { return this.owner; }

    public String getOwnerUsername() {
        return this.owner;
    }

    public void setOwner(String owner) { this.owner = owner; }

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

    public void answerClue(Clue clue) {
        int index = this.clues.indexOf(clue);
        this.clues.get(index).setAnswered();
        if (this.findFirstUnansweredClue() == null) {
            this.setCompleted();
        }
    }
    // Finds first unanswered clue
    public Clue findFirstUnansweredClue() {
        Clue clue = null;
        for (Clue c : this.clues) {
            if (!c.isAnswered()) {
                clue = c;
                break;
            }
        }
        return clue;
    }

    public boolean isCompleted() {
        return  this.completed;
//        boolean isCompleted = true;
//        for(Clue clue: this.clues) {
//            if (!clue.isAnswered()) {
//                isCompleted = false;
//                break;
//            }
//        }
//
//        return  isCompleted;
    }

}

package com.mosis.treasurehunt.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;

public class Clue {
    private String mQuestion;
    public HashMap <String, Boolean> answers; // answer - true/false if correct
    private User owner;
    private double mLatitude;
    private double mLongitude;
    @Exclude
    private String mKey;

    public Clue() {
        answers = new HashMap<>();
    }

    public Clue(String question, User user) {
        this.mQuestion = question;
        this.owner = user;
        answers = new HashMap<>();
    }

    public void addAnswer(String answer, Boolean correct) {
        this.answers.put(answer, correct);
    }

    public String getKey() { return this.mKey; }

    public void setKey(String key) { this.mKey = key; }

    public String getmQuestion() { return this.mQuestion; }

    public void setmQuestion(String question) { this.mQuestion = question; }

    private double getmLatitude() { return this.mLatitude; }

    private void setmLatitude(double latitude) { this.mLatitude = latitude; }

    private double getmLongitude() { return  this.mLongitude; }

    private void setmLongitude(double longitude) { this.mLongitude = longitude; }

}

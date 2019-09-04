package com.mosis.treasurehunt.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;

public class Clue {
    private String question;
    public HashMap <String, Boolean> answers; // answer - true/false if correct
    private Hunt parentHunt;
    private double latitude;
    private double longitude;
    private Clue nextClue; // null if this clue is the last Clue
    private boolean answered;
    @Exclude
    private String key;

    public Clue() {
        answers = new HashMap<>();
    }

    public Clue(String question, Hunt hunt) {
        this();
        this.question = question;
        this.parentHunt = hunt;
        this.nextClue = null;
        this.answered = false;
    }

    public Clue(String question) {
        this();
        this.question = question;
        this.nextClue = null;
        this.answered = false;
    }

    public void addAnswer(String answer, Boolean correct) {
        this.answers.put(answer, correct);
    }

    public String getKey() { return this.key; }

    public void setKey(String key) { this.key = key; }

    public String getQuestion() { return this.question; }

    public void setQuestion(String question) { this.question = question; }

    public double getLatitude() { return this.latitude; }

    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return  this.longitude; }

    public void setLongitude(double longitude) { this.longitude = longitude; }

    public void setNextClue(Clue clue) { this.nextClue = clue; }

    public Clue getNextClue() { return this.nextClue; }

    public boolean isAnswered() { return this.answered; }

    public void setAnswered() {
        this.answered = true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if(!(o instanceof Clue)) {
            return false;
        }

        Clue clue = (Clue) o;
        return question.equals(clue.getQuestion());
    }

    public HashMap<String, Boolean> getAnswers() {
        return answers;
    }
}

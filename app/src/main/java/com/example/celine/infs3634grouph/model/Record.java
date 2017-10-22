package com.example.celine.infs3634grouph.model;

/**
 * Created by Celine on 22/10/2017.
 */

public class Record {
    private int userID;
    private String time;
    private int difficulty;
    private int speed;
    private int category;
    private int score;

    private static final int SPEED_SLOW = 0;
    private static final int SPEED_MED = 1;
    private static final int SPEED_FAST = 2;

    private static final int DIFF_EASY = 0;
    private static final int DIFF_MED = 1;
    private static final int DIFF_HARD = 2;

    public Record() {
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getDate() {
        return time;
    }

    public void setDate(String date) {
        this.time = date;
    }

    public String getDifficulty() {
        String result = "";
        switch (this.difficulty){
            case DIFF_EASY:
                result = "Easy";
                break;
            case DIFF_MED:
                result = "Medium";
                break;
            case DIFF_HARD:
                result = "Hard";
                break;
        }
        return result;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public String getSpeed() {
        String result = "";
        switch (this.speed){
            case SPEED_SLOW:
                result = "Slow";
                break;
            case SPEED_MED:
                result = "Medium";
                break;
            case SPEED_FAST:
                result = "Fast";
                break;
        }
        return result;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


}

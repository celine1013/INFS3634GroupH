package com.example.celine.infs3634grouph.model;

/**
 * Created by Celine on 25/09/2017.
 */

public class Question {
    int questionID;
    private String content;
    private int difficulty;
    private int category;
    private String[] answerOptions;
    private int trueAnswer;

    private int score;

    public static final int EASY = 1;
    public static final int MED = 2;
    public static final int HARD = 3;

    public static final int CATE_O1_GENERAL = 1;
    public static final int CATE_O2_FUNDAMENTAL = 2;
    public static final int CATE_O3_GOOGLE = 3;
    public static final int CATE_O4_ACTIVITY = 4;
    public static final int CATE_O5_FRAGMENT = 5;
    public static final int CATE_O6_INTENT = 6;
    public static final int CATE_O7_DATABASE = 7;

    //empty constructor
    public Question() {
    }

    public Question(int questionID, String content, int difficulty, int category, String[] answerOptions, int trueAnswer) {
        this.questionID = questionID;
        this.content = content;
        this.difficulty = difficulty;
        this.category = category;
        this.answerOptions = answerOptions;
        this.trueAnswer = trueAnswer;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String[] getAnswerOptions() {
        return answerOptions;
    }

    public void setAnswerOptions(String[] answerOptions) {
        this.answerOptions = answerOptions;
    }

    public int getTrueAnswer() {
        return trueAnswer;
    }

    public void setTrueAnswer(int trueAnswer) {
        this.trueAnswer = trueAnswer;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}

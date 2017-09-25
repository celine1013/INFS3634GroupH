package model;

/**
 * Created by Celine on 25/09/2017.
 */

public class Question {
    int questionID;
    private String content;
    private int difficulty;
    private int category;

    //empty constructor
    public Question() {
    }

    //for preloading
    public Question(String content, int difficulty, int category) {
        this.content = content;
        this.difficulty = difficulty;
        this.category = category;
    }

    //for retrieving
    public Question(int questionID, String content, int category, int difficulty) {
        this.questionID = questionID;
        this.content = content;
        this.difficulty = difficulty;
        this.category = category;
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
}

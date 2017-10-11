package model;

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

    //empty constructor
    public Question() {
    }

    //for preloading
    public Question(String content, int difficulty, int category, String[] answerOptions, int trueAnswer) {
        this.content = content;
        this.difficulty = difficulty;
        this.category = category;
        this.answerOptions = answerOptions;
        this.trueAnswer = trueAnswer;
    }

    //for retrieving
    public Question(int questionID, String content, int difficulty, int category, String[] answerOptions,
                    int trueAnswer) {
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

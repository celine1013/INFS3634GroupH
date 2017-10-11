package com.example.celine.infs3634grouph;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

import dbHelper.DatabaseHelper;
import model.Question;


public class QuizActivity extends AppCompatActivity implements View.OnClickListener{
    private DatabaseHelper db;
    // TODO: 26/09/2017 declare widget fields in all layouts used in this activity

    private int maxQuestionNum;
    private int correctNum;
    private int currentCorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // TODO: 26/09/2017 binding

        // TODO: 26/09/2017 get questions using category data sent by main activity
        // specify a number of random questions within the specific category
        int category = this.getIntent().getIntExtra(MainActivity.TAG_CATEGORY, -1);//get category from intent
        //catch unknown error
        if(category == -1){
            Log.e("QUIZ ACTIVITY", "NO CATEGORY RECEIVED, ACTIVITY FINISHED");
            finish();
        }

        List<Question> questions ;//= db.getQuestionsByCategory(category);
        // TODO: 26/09/2017 perform quiz activity
        // show question
        // show answer options
        // verify if the user has chose the correct answer
        // if incorrect, show the correct one immediately
        // calculate score, change the text field
        maxQuestionNum = questions.size();
        Question q = questions.get(0);
        // TODO: 7/10/2017 show the first question and answer options

        currentCorrect = q.getTrueAnswer();

        // TODO: 26/09/2017 calculate and show result
        // change layout
        setContentView(R.layout.activity_result);
        // TODO: 7/10/2017 bind new layout
        // show final score, etc.
        // btn: finish->go back to main activity;
        // btn: again->start the quiz again
    }

    @Override
    public void onClick(View view) {
        int answerChosed = -1;
        switch (view.getId()) {
            case R.id.btnAnswerA:
                answerChosed = 1;
                break;
            case R.id.btnAnswerB:
                answerChosed = 2;
                break;
            case R.id.btnAnswerC:
                answerChosed = 3;
                break;
            case R.id.btnAnswerD:
                answerChosed = 4;
                break;
            default:
                // TODO: 7/10/2017 veryfy if answer chosed match currentAnswer

                //if matched, currentCorrect increase, show correct notification, score changed
                //if mismatched, show incorrect notification

                //show true answer no matter whether the user answer correctly
                //true answer btn's background color turns green, others turn green
        }
    }
}

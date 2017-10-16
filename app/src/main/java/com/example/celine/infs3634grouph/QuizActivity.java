package com.example.celine.infs3634grouph;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;
import dbHelper.DatabaseHelper;
import model.Question;


public class QuizActivity extends AppCompatActivity implements View.OnClickListener{
    private DatabaseHelper db;
    // TODO: 26/09/2017 declare widget fields in all layouts used in this activity
    private TextView show_category;
    private TextView show_questionNumber;
    private TextView show_score;
    private TextView show_question;
    private Button btn_answerA;
    private Button btn_answerB;
    private Button btn_answerC;
    private Button btn_answerD;
    private Button next_question;
    private Button btn_tryAgain;
    private Button btn_tryAnother;

    private int maxQuestionNum;
    private int correctNum;
    private int currentCorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // TODO: 26/09/2017 binding
        show_category = (TextView)findViewById(R.id.textViewCategory);
        show_questionNumber = (TextView)findViewById(R.id.questionNum);
        show_score = (TextView)findViewById(R.id.textViewScore);
        show_question = (TextView)findViewById(R.id.question_text_view);
        btn_answerA = (Button)findViewById(R.id.btnAnswerA);
        btn_answerB = (Button)findViewById(R.id.btnAnswerB);
        btn_answerC = (Button)findViewById(R.id.btnAnswerC);
        btn_answerD = (Button)findViewById(R.id.btnAnswerD);
        next_question = (Button) findViewById(R.id.nextQuestion);
        btn_tryAgain = (Button)findViewById(R.id.btnTryAgain);
        btn_tryAnother = (Button)findViewById(R.id.btnTryAnother);

        // TODO: 26/09/2017 get questions using category data sent by main activity
        // specify a number of random questions within the specific category
        int category = this.getIntent().getIntExtra(MainActivity.TAG_CATEGORY, -1);//get category from intent
        //catch unknown error
        if(category == -1){
            Log.e("QUIZ ACTIVITY", "NO CATEGORY RECEIVED, ACTIVITY FINISHED");
            finish();
        }

        List<Question> questions = db.getQuestionsByCategory(category);
        // TODO: 26/09/2017 perform quiz activity
        Question question = new Question();
        // show category
        show_category.setText(getResources().getString(category));
        // show question number and want to use the number 1-10
        show_questionNumber.setText(question.getQuestionID());
        // show question
        show_question.setText(question.getContent());
        // show answer options
        
        // verify if the user has chose the correct answer
        // if incorrect, show the correct one immediately
        // calculate score, change the text field
        //maxQuestionNum = questions.size();
        //Question q = questions.get(0);
        // TODO: 7/10/2017 show the first question and answer options

        //currentCorrect = q.getTrueAnswer();

        // TODO: 26/09/2017 calculate and show result
        // change layout
        setContentView(R.layout.activity_result);
        // TODO: 7/10/2017 bind new layout
        // show final score, etc.

        // btn: finish->go back to main activity;
        btn_tryAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // btn: again->start the quiz again
        btn_tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
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

package com.example.celine.infs3634grouph;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Currency;
import java.util.List;
import com.example.celine.infs3634grouph.dbHelper.DatabaseHelper;
import com.example.celine.infs3634grouph.dbHelper.QuestionProvider;
import com.example.celine.infs3634grouph.model.Question;


public class QuizActivity extends AppCompatActivity implements View.OnClickListener{

    private DatabaseHelper db;

    //declare widget fields in all layouts used in this activity
    private TextView show_category;
    private TextView show_questionNumber;
    private TextView show_score;
    private TextView show_question;
    private Button btn_answerA;
    private Button btn_answerB;
    private Button btn_answerC;
    private Button btn_answerD;
    
    private Button next_question;
    private TextView show_result;
    private Button btn_tryAgain;
    private Button btn_tryAnother;

    //declare
    private static final int MAX_QUESTION_NUM = 10;
    private int correctNum;
    private int currentCorrect;
    private int currentNum = 0;
    private List<Question> questions;
    private String[] options;
    private int scoreTotal = 0;
    private int curSocre = 10;
    private Question q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // binding
        show_category = (TextView)findViewById(R.id.textViewCategory);
        show_questionNumber = (TextView)findViewById(R.id.questionNum);
        show_score = (TextView)findViewById(R.id.textViewScore);
        show_question = (TextView)findViewById(R.id.question_text_view);
        btn_answerA = (Button)findViewById(R.id.btnAnswerA);
        btn_answerB = (Button)findViewById(R.id.btnAnswerB);
        btn_answerC = (Button)findViewById(R.id.btnAnswerC);
        btn_answerD = (Button)findViewById(R.id.btnAnswerD);
        next_question = (Button) findViewById(R.id.nextQuestion);

        // specify a number of random questions within the specific category
        int category = this.getIntent().getIntExtra(MainActivity.TAG_CATEGORY, -1);//get category from intent
        //catch unknown error
        if(category == -1){
            Log.e("QUIZ ACTIVITY", "NO CATEGORY RECEIVED, ACTIVITY FINISHED");
            finish();
        }
        // show category
        show_category.setText(getResources().getString(category));
        db = new DatabaseHelper(this);
        final List<Question> questions = db.getQuestionsByCategory(category);
        

        // calculate score, change the text field
        show_score.setText(scoreTotal);


        btn_answerA.setOnClickListener(this);
        btn_answerB.setOnClickListener(this);
        btn_answerC.setOnClickListener(this);
        btn_answerD.setOnClickListener(this);

        next_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //show_score.setText(score)
                if(currentNum < questions.size()){
                    currentNum ++;
                    showQuestion();
                    show_score.setText(scoreTotal);
                    
                }else{
                    currentNum = 0;
                    showresult();
                }
                //这个get(1）肯定不对不知道咋弄
                
            }
        });

        
    }

    @Override
    public void onClick(View view) {
        int answerChose = -1;
        int correctButton = 0;
        switch (view.getId()) {
            case R.id.btnAnswerA:
                answerChose = 1;
                break;
            case R.id.btnAnswerB:
                answerChose = 2;
                break;
            case R.id.btnAnswerC:
                answerChose = 3;
                break;
            case R.id.btnAnswerD:
                answerChose = 4;
                break;
            default:
                // 7/10/2017 veryfy if answer chosed match currentAnswer
                //if matched, currentCorrect increase, show correct notification, score changed
                //if mismatched, show incorrect notification
                // 按我的逻辑是if((answerChose-1).equals(q.getTrueAnswer))
                // 如果(answerChose-1).equals(q.getTrueAnswer)就是对的，然后score = score + 10
                if(answerChose == currentCorrect){
                    correctNum++;
                    scoreTotal += curSocre;
                    correctButton = currentCorrect;
                    Toast.makeText(QuizActivity.this, getResources().getString(R.string.correct_notification), Toast.LENGTH_SHORT);
                }else{
                    Toast.makeText(QuizActivity.this, getResources().getString(R.string.incorrect_notification), Toast.LENGTH_SHORT);
                }
                //show true answer no matter whether the user answer correctly
                //true answer btn's background color turns green, others turn green
                //score change

                switch (currentCorrect){
                    case 1:
                        correctButton = R.id.btnAnswerA;
                        break;
                    case 2:
                        correctButton = R.id.btnAnswerB;
                        break;
                    case 3:
                        correctButton = R.id.btnAnswerC;
                        break;
                    case 4:
                        correctButton = R.id.btnAnswerD;
                        break;
                }
                // Button变颜色的不知道怎么弄
                btn_answerA.setBackgroundColor(getColor(R.color.colorRed));
                btn_answerB.setBackgroundColor(getColor(R.color.colorRed));
                btn_answerC.setBackgroundColor(getColor(R.color.colorRed));
                btn_answerD.setBackgroundColor(getColor(R.color.colorRed));
                Button btn_choose = (Button)findViewById(correctButton);
                btn_choose.setBackgroundColor(getColor(R.color.colorGreen));

        }
    }
    
    private void showQuestion(){
        // show the first question and answer options
        //maxQuestionNum = questions.size();
        //说实话不懂这个get(0);
        q = questions.get(currentNum);
        currentCorrect = q.getTrueAnswer();
        // show question number
        show_questionNumber.setText(currentNum);
        // get questions using category data sent by main activity
        show_question.setText(q.getContent());

        options = q.getAnswerOptions();
        // show answer options???????? 这个要把String[]的东西按照[0][1][2][3]的方式放进去，但是试了好多不知道怎么办了
        btn_answerA.setText(options[0]);
        btn_answerB.setText(options[1]);
        btn_answerC.setText(options[2]);
        btn_answerD.setText(options[3]);
    }
    
    private void showresult(){
        // change layout
        setContentView(R.layout.activity_result);
        // bind new layout
        show_result = (TextView) findViewById(R.id.showResult);
        btn_tryAgain = (Button)findViewById(R.id.btnTryAgain);
        btn_tryAnother = (Button)findViewById(R.id.btnTryAnother);

        // show final score, etc.
        // TODO: 17/10/2017 change the layout of result: score, correct num
        show_result.setText(scoreTotal);
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
                // change layout
                setContentView(R.layout.activity_quiz);
                //找到此时的Category，然后重新建立所有textView
                /*Intent intent = getIntent();
                finish();
                startActivity(intent);*/
                QuizActivity.this.recreate();
            }
        });
    }
}

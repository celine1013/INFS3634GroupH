package com.example.celine.infs3634grouph;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import com.example.celine.infs3634grouph.dbHelper.DatabaseContract;
import com.example.celine.infs3634grouph.dbHelper.DatabaseHelper;
import com.example.celine.infs3634grouph.dbHelper.QuestionProvider;
import com.example.celine.infs3634grouph.model.Question;

import static java.lang.Math.*;


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
    private TextView show_correctNum;

    private Vibrator vibrator;
    private MediaPlayer mp;
    private BackgroundSound bgm;


    //declare
    private static final int MAX_QUESTION_NUM = 10;
    private int correctNum;
    private int currentCorrect = 0;
    private int currentNum = 0;
    private List<Question> questions_l;
    private String[] options;
    private int scoreTotal = 0;
    private int curSocre = 20;
    private Question q;
    private ContentResolver cr;
    private int progress = 0;
    private int time = 10000;

    private ProgressBar pb_countDown;
    private CountDownTimer countDownTimer_showAnswer;

    private static final int SP_FAST = 5000;
    private static final int SP_MED = 10000;
    private static final int SP_SLOW = 15000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        // binding
        show_category = (TextView) findViewById(R.id.textViewCategory);
        show_questionNumber = (TextView) findViewById(R.id.questionNum);
        show_score = (TextView) findViewById(R.id.textViewScore);
        show_question = (TextView) findViewById(R.id.question_text_view);
        btn_answerA = (Button) findViewById(R.id.btnAnswerA);
        btn_answerB = (Button) findViewById(R.id.btnAnswerB);
        btn_answerC = (Button) findViewById(R.id.btnAnswerC);
        btn_answerD = (Button) findViewById(R.id.btnAnswerD);

        pb_countDown = findViewById(R.id.progressBar_countDown);

        cr = getContentResolver();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // specify a number of random questions within the specific category
        int category_str = this.getIntent().getIntExtra(MainActivity.TAG_CATEGORY_SHOW, -1);//get category from intent
        //catch unknown error
        if (category_str == -1) {
            Log.e("QUIZ ACTIVITY", "NO CATEGORY RECEIVED, ACTIVITY FINISHED");
            finish();
        }
        // show category
        show_category.setText(getResources().getString(category_str));

        int category_dat = this.getIntent().getIntExtra(MainActivity.TAG_CATEGORY_DATA, -1);
        if (category_dat == Question.CATE_RANDOM) {
            questions_l = getAllQuestions();
        } else {
            questions_l = getQuestionsByCategory(category_dat);
        }


        // calculate score, change the text field
        show_score.setText(String.valueOf(scoreTotal));
        btn_answerA.setOnClickListener(this);
        btn_answerB.setOnClickListener(this);
        btn_answerC.setOnClickListener(this);
        btn_answerD.setOnClickListener(this);

        /*next_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //show_score.setText(score)
                if(currentNum + 1 < questions_l.size()){
                    currentNum ++;
                    showQuestion();
                    show_score.setText(String.valueOf(scoreTotal));
                    
                }else{
                    currentNum = 0;
                    showresult();
                }
            }
        });*/
        showQuestion();
        int speed = getIntent().getIntExtra(MainActivity.TAG_SPEED, -1);
        switch (speed){
            case MainActivity.SPEED_FAST:
                time = SP_FAST;
                break;
            case MainActivity.SPEED_MEDIUM:
                time = SP_MED;
                break;
            case MainActivity.SPEED_SLOW:
                time = SP_SLOW;
                break;
        }
        countDownTimer_showAnswer = new CountDownTimer(time, 100) {
            @Override
            public void onTick(long l) {

                pb_countDown.setProgress((int)progress*100/(time/100));
                progress++;
            }

            @Override
            public void onFinish() {
                pb_countDown.setProgress(0);
                progress = 0;
                if (currentNum< questions_l.size()) {
                    showQuestion();
                    this.start();
                } else {
                    currentNum = 0;
                    showresult();
                }
            }
        };
        countDownTimer_showAnswer.start();


    }
    @Override
    public void onResume(){
        super.onResume();
        bgm = (BackgroundSound)new BackgroundSound().execute();
    }
    @Override
    public void onPause(){
        super.onPause();
        mp.stop();
        bgm.cancel(true);
    }
    public class BackgroundSound extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            mp = MediaPlayer.create(QuizActivity.this, R.raw.numb);
            mp.setLooping(true); // Set looping
            mp.setVolume(2.0f, 2.0f);
            mp.start();

            return null;
        }

        @Override
        protected void onCancelled() {
            mp.stop();
        }

    }

    @Override
    public void onClick(View view) {
        Log.d("ANSWERING", "ANSWER CLICKED");
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
        }
        // 7/10/2017 verify if answer chose match currentAnswer
        //if matched, currentCorrect increase, show correct notification, score changed
        //if mismatched, show incorrect notification
        if (answerChose == currentCorrect) {
            correctNum++;
            scoreTotal += curSocre;
            correctButton = currentCorrect;

            //Toast.makeText(QuizActivity.this, getResources().getString(R.string.correct_notification), Toast.LENGTH_LONG).show();
        } else {
            vibrator.vibrate(600);
            //Toast.makeText(QuizActivity.this, getResources().getString(R.string.incorrect_notification), Toast.LENGTH_LONG).show();
        }
        //show true answer no matter whether the user answer correctly
        //true answer btn's background color turns green, others turn green
        //score change
        switch (currentCorrect) {
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
        btn_answerA.setBackgroundColor(getColor(R.color.colorRed));
        btn_answerB.setBackgroundColor(getColor(R.color.colorRed));
        btn_answerC.setBackgroundColor(getColor(R.color.colorRed));
        btn_answerD.setBackgroundColor(getColor(R.color.colorRed));
        btn_answerA.setClickable(false);
        btn_answerB.setClickable(false);
        btn_answerC.setClickable(false);
        btn_answerD.setClickable(false);
        Button btn_choose = (Button) findViewById(correctButton);
        btn_choose.setBackgroundColor(getColor(R.color.colorGreen));
        CountDownTimer ct = new CountDownTimer(1500, 250) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                countDownTimer_showAnswer.onFinish();
            }
        }.start();

    }

    
    private void showQuestion(){
        // show the first question and answer options
        //maxQuestionNum = questions.size();
        show_score.setText(String.valueOf(scoreTotal));
        q = questions_l.get(currentNum);
        currentCorrect = q.getTrueAnswer();
        // show question number
        show_questionNumber.setText(String.valueOf(currentNum+ 1 ));
        // get questions using category data sent by main activity
        show_question.setText(q.getContent());

        options = q.getAnswerOptions();
        // show answer options
        btn_answerA.setText(options[0]);
        btn_answerB.setText(options[1]);
        btn_answerC.setText(options[2]);
        btn_answerD.setText(options[3]);

        btn_answerA.setBackgroundColor(getColor(R.color.colorBlack));
        btn_answerB.setBackgroundColor(getColor(R.color.colorBlack));
        btn_answerC.setBackgroundColor(getColor(R.color.colorBlack));
        btn_answerD.setBackgroundColor(getColor(R.color.colorBlack));
        btn_answerA.setClickable(true);
        btn_answerB.setClickable(true);
        btn_answerC.setClickable(true);
        btn_answerD.setClickable(true);

        currentNum++;
    }
    
    private void showresult(){
        // change layout
        setContentView(R.layout.activity_result);
        // bind new layout
        show_result = (TextView) findViewById(R.id.showResult);
        btn_tryAgain = (Button)findViewById(R.id.btnTryAgain);
        btn_tryAnother = (Button)findViewById(R.id.btnTryAnother);
        show_correctNum = (TextView)findViewById(R.id.showCorrectNum);

        // show final score, etc.
        // TODO: 17/10/2017 change the layout of result: score, correct num
        show_result.setText(String.valueOf(scoreTotal));
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
                /*Intent intent = getIntent();
                finish();
                startActivity(intent);*/
                QuizActivity.this.recreate();
            }
        });
        // show correct answer number x/5
        show_correctNum.setText(String.valueOf(correctNum));
    }

    public List<Question> getAllQuestions() {
        int diff = getIntent().getIntExtra(MainActivity.TAG_DIFF, -1);
        String selection = DatabaseContract.QuestionEntry.QUESTION_DIFFICULTY + " = " + diff;
        Cursor c = cr.query(QuestionProvider.CONTENT_URI, DatabaseContract.QuestionEntry.QUESTION_ALL_COLUMNS, selection, null, "RANDOM()");
        List<Question> questions = new ArrayList<>();
        // looping through all rows and adding to list
        if(c != null) {
            if (c.moveToFirst()) {
                do {
                    Question q = new Question();
                    q.setQuestionID(c.getInt(c.getColumnIndex(DatabaseContract.QuestionEntry.KEY_QUESTION_ID)));
                    q.setContent(c.getString(c.getColumnIndex(DatabaseContract.QuestionEntry.QUESTION_CONTENT)));
                    q.setCategory(c.getInt(c.getColumnIndex(DatabaseContract.QuestionEntry.QUESTION_CATEGORY)));
                    q.setDifficulty(c.getInt(c.getColumnIndex(DatabaseContract.QuestionEntry.QUESTION_DIFFICULTY)));
                    q.setTrueAnswer(c.getInt(c.getColumnIndex(DatabaseContract.QuestionEntry.QUESTION_TRUE_ANSWER)));

                    String[] options = new String[4];
                    options[0] = c.getString(c.getColumnIndex(DatabaseContract.QuestionEntry.QUESTION_ANSWER_OPTIONS_1));
                    options[1] = c.getString(c.getColumnIndex(DatabaseContract.QuestionEntry.QUESTION_ANSWER_OPTIONS_2));
                    options[2] = c.getString(c.getColumnIndex(DatabaseContract.QuestionEntry.QUESTION_ANSWER_OPTIONS_3));
                    options[3] = c.getString(c.getColumnIndex(DatabaseContract.QuestionEntry.QUESTION_ANSWER_OPTIONS_4));
                    q.setAnswerOptions(options);
                    // adding to the list
                    questions.add(q);
                } while (c.moveToNext());
            }
        }
        c.close();
        return questions;
    }

    public List<Question> getQuestionsByCategory(int category) {
        List<Question> questions = new ArrayList<>();
        int difficulty = getIntent().getIntExtra(MainActivity.TAG_DIFF,-1);
        String str1 = DatabaseContract.QuestionEntry.QUESTION_DIFFICULTY + " = " + difficulty;
        String str2 = DatabaseContract.QuestionEntry.QUESTION_CATEGORY + " = " + category;
        String selection = str1 + " AND " + str2;
        Cursor c = cr.query(QuestionProvider.CONTENT_URI, DatabaseContract.QuestionEntry.QUESTION_ALL_COLUMNS, selection, null, "RANDOM() LIMIT 5");

        // looping through all rows and adding to list
        if(c != null) {
            if (c.moveToFirst()) {
                do {
                    Question q = new Question();
                    q.setQuestionID(c.getInt(c.getColumnIndex(DatabaseContract.QuestionEntry.KEY_QUESTION_ID)));
                    q.setContent(c.getString(c.getColumnIndex(DatabaseContract.QuestionEntry.QUESTION_CONTENT)));
                    q.setCategory(c.getInt(c.getColumnIndex(DatabaseContract.QuestionEntry.QUESTION_CATEGORY)));
                    q.setDifficulty(c.getInt(c.getColumnIndex(DatabaseContract.QuestionEntry.QUESTION_DIFFICULTY)));
                    q.setTrueAnswer(c.getInt(c.getColumnIndex(DatabaseContract.QuestionEntry.QUESTION_TRUE_ANSWER)));

                    String[] options = new String[4];
                    options[0] = c.getString(c.getColumnIndex(DatabaseContract.QuestionEntry.QUESTION_ANSWER_OPTIONS_1));
                    options[1] = c.getString(c.getColumnIndex(DatabaseContract.QuestionEntry.QUESTION_ANSWER_OPTIONS_2));
                    options[2] = c.getString(c.getColumnIndex(DatabaseContract.QuestionEntry.QUESTION_ANSWER_OPTIONS_3));
                    options[3] = c.getString(c.getColumnIndex(DatabaseContract.QuestionEntry.QUESTION_ANSWER_OPTIONS_4));
                    q.setAnswerOptions(options);
                    // adding to the list
                    questions.add(q);
                } while (c.moveToNext());
            }
        }
        c.close();
        return questions;
    }
}

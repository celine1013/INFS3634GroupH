package com.example.celine.infs3634grouph;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.List;

import com.example.celine.infs3634grouph.dbHelper.DatabaseContract;
import com.example.celine.infs3634grouph.dbHelper.DatabaseHelper;
import com.example.celine.infs3634grouph.dbHelper.QuestionProvider;
import com.example.celine.infs3634grouph.dbHelper.RecordProvider;
import com.example.celine.infs3634grouph.model.Question;
import com.example.celine.infs3634grouph.model.Record;

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


    //declare variables used
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
    private int category_dat;
    private int category_str;
    private int speed;
    private int diff;
    private boolean isCorrect = true;

    private ProgressBar pb_countDown;
    private CountDownTimer countDownTimer_showAnswer;

    private static final int SP_FAST = 12000;
    private static final int SP_MED = 18000;
    private static final int SP_SLOW = 24000;
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
        category_str = this.getIntent().getIntExtra(MainActivity.TAG_CATEGORY_SHOW, -1);//get category from intent
        //catch unknown error
        if (category_str == -1) {
            Log.e("QUIZ ACTIVITY", "NO CATEGORY RECEIVED, ACTIVITY FINISHED");
            finish();
        }
        // show category
        show_category.setText(getResources().getString(category_str));

        //get question list from database
        category_dat = this.getIntent().getIntExtra(MainActivity.TAG_CATEGORY_DATA, -1);
        if (category_dat == Question.CATE_RANDOM) {
            questions_l = getAllQuestions();
        } else {
            questions_l = getQuestionsByCategory(category_dat);
        }


        // set up onClickListener
        show_score.setText(String.valueOf(scoreTotal));
        btn_answerA.setOnClickListener(this);
        btn_answerB.setOnClickListener(this);
        btn_answerC.setOnClickListener(this);
        btn_answerD.setOnClickListener(this);

        showQuestion();
        speed = getIntent().getIntExtra(MainActivity.TAG_SPEED, -1);
        if(speed == -1){
            Log.e("QUIZ SPEED", "SPEED NOT FOUND");
        }

        //change backgound music depends on speed
        switch (speed){
            case MainActivity.SPEED_FAST:
                time = SP_FAST;
                mp = MediaPlayer.create(QuizActivity.this, R.raw.fast);
                break;
            case MainActivity.SPEED_MEDIUM:
                mp = MediaPlayer.create(QuizActivity.this, R.raw.medium);
                time = SP_MED;
                break;
            case MainActivity.SPEED_SLOW:
                mp = MediaPlayer.create(QuizActivity.this, R.raw.slow);
                time = SP_SLOW;
                break;
        }

        //play background music
        mp.setLooping(true);
        mp.setVolume(2.0f, 2.0f);
        mp.start();

        //setup and start the timer
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

                //if there are more questions, jump to next
                //if quiz ends, show quiz result
                if (currentNum< questions_l.size()) {
                    showQuestion();
                    this.start();
                } else {
                    showresult();
                }
            }
        };
        countDownTimer_showAnswer.start();


    }

    @Override
    public void onPause(){
        super.onPause();
        mp.stop();
    }

    @Override
    public void onClick(View view) {
        Log.v("ANSWERING", "ANSWER CLICKED");
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
        //verify if answer chose match currentAnswer
        if (answerChose == currentCorrect) {
            correctNum++;
            scoreTotal += curSocre;
            correctButton = currentCorrect;

            //Toast.makeText(QuizActivity.this, getResources().getString(R.string.correct_notification), Toast.LENGTH_LONG).show();
        } else {
            vibrator.vibrate(600);
            isCorrect = false;
            //Toast.makeText(QuizActivity.this, getResources().getString(R.string.incorrect_notification), Toast.LENGTH_LONG).show();
        }
        //show true answer no matter whether the user answer correctly
        //true answer btn's background color turns green, others turn red
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

        //the answer options can only be clicked once
        btn_answerA.setClickable(false);
        btn_answerB.setClickable(false);
        btn_answerC.setClickable(false);
        btn_answerD.setClickable(false);
        Button btn_choose = (Button) findViewById(correctButton);
        //change the correct one's color
        btn_choose.setBackgroundColor(getColor(R.color.colorGreen));

        //stop the original timer
        //prevent calling onFinish when ct is still working
        countDownTimer_showAnswer.cancel();
        //create another timer for showing correct answer, then automatically jump to next question
        CountDownTimer ct = new CountDownTimer(2000, 250) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                //if game mode is quick start, the quiz ends whenever players answer incorrectly
                if(category_dat == Question.CATE_RANDOM && !isCorrect){
                    showresult();
                }else {
                    countDownTimer_showAnswer.onFinish();
                }
            }
        }.start();
    }

    //show question on the layout
    private void showQuestion(){
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

        //reset color and clickable state
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
        // show correct answer number
        show_correctNum.setText(String.valueOf(correctNum) + "/" + String.valueOf(currentNum));

        //save record
        Record r = new Record();
        r.setCategory(category_str);
        r.setSpeed(speed);
        r.setDifficulty(diff);
        r.setScore(scoreTotal);

        DateFormat df = new SimpleDateFormat("MMM.dd 'at' HH:mm");
        String date = df.format(Calendar.getInstance().getTime());
        r.setDate(date);
        createRecord(r);

        //if users reached new highest score, show notification using alertdialog
        if(isHighest(scoreTotal)){
            NotificationDialog dialog = new NotificationDialog();
            dialog.show(getSupportFragmentManager(),"NOTIFICATION DIALOG");

        }
    }

    //get questions regardless of categories with random order
    public List<Question> getAllQuestions() {
        diff = getIntent().getIntExtra(MainActivity.TAG_DIFF, -1);
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

    //get questions with different categories, the number of questions is limited to 5
    public List<Question> getQuestionsByCategory(int category) {
        List<Question> questions = new ArrayList<>();
        diff = getIntent().getIntExtra(MainActivity.TAG_DIFF,-1);
        String str1 = DatabaseContract.QuestionEntry.QUESTION_DIFFICULTY + " = " + diff;
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

    public Uri createRecord(Record record) {
        ContentValues values = new ContentValues();

        values.put(DatabaseContract.RecordEntry.RECORD_CATE, record.getCategory());
        values.put(DatabaseContract.RecordEntry.RECORD_SPEED, record.getSpeed());
        values.put(DatabaseContract.RecordEntry.RECORD_DIFF, record.getDifficulty());
        values.put(DatabaseContract.RecordEntry.RECORD_SCORE, record.getScore());
        values.put(DatabaseContract.RecordEntry.RECORD_TIME, record.getDate());
        // insert row
        Uri id = cr.insert(RecordProvider.CONTENT_URI,values);

        return id;
    }

    //verify if the users create a new highest score
    public boolean isHighest(int score){
        boolean result = false;
        int s = 0;
        Cursor c = cr.query(RecordProvider.CONTENT_URI, new String[]{"MAX("
                + DatabaseContract.RecordEntry.RECORD_SCORE
                +")"}, null, null, null);

        if (c.moveToFirst()) {
            s = c.getInt(c.getColumnIndex(DatabaseContract.RecordEntry.RECORD_SCORE));
        }
        if (s <= score) result = true;
        c.close();
        return result;
    }

    public static class NotificationDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.dialog_title);
            builder.setMessage(R.string.dialog_message);
            builder.setPositiveButton("View History", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(getActivity(), PastRecordActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(R.string.text_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
}

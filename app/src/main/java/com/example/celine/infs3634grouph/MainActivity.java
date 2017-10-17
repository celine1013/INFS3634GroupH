package com.example.celine.infs3634grouph;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import com.example.celine.infs3634grouph.dbHelper.DatabaseContract;
import com.example.celine.infs3634grouph.dbHelper.DatabaseHelper;
import com.example.celine.infs3634grouph.dbHelper.QuestionProvider;
import com.example.celine.infs3634grouph.model.Question;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    DatabaseHelper db;
    private static final int MAX_QUESTION = 50;
    private static final int MAX_CAT1 = 10;
    private static final int MAX_CAT2 = 20;
    private static final int MAX_CAT3 = 30;
    private static final int MAX_CAT4 = 40;
    private static final int MAX_CAT5 = 50;

    //declare integer used in building intents
    public static final int CATEGORY_RANDOM = R.string.category_random;
    public static final int CATEGORY_01 = R.string.category_01;
    public static final int CATEGORY_02 = R.string.category_02;
    // TODO: 7/10/2017 complete below:
    public static final int CATEGORY_03 = R.string.category_03;
    public static final int CATEGORY_04 = R.string.category_04;
    public static final int CATEGORY_05 = R.string.category_05;
    public static final int CATEGORY_06 = R.string.category_06;
    public static final int CATEGORY_07 = R.string.category_07;

    // TODO: 7/10/2017 declare all widgets
    private Button btn_quickStart;
    private Button btn_general;
    private Button btn_fundamentals;
    private Button btn_google;
    private Button btn_activities;
    private Button btn_fragments;
    private Button btn_intent;
    private Button btn_database;

    public static final String TAG_CATEGORY_SHOW = "category_str";
    public static final String TAG_CATEGORY_DATA = "category_dat";
    private ContentResolver cr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setDatabase();
        // TODO: 7/10/2017 complete binding
        btn_quickStart = (Button)findViewById(R.id.quickStartBtn);
        btn_general = (Button)findViewById(R.id.btnGeneral);
        btn_fundamentals = (Button)findViewById(R.id.btnFundamentals);
        btn_fragments = (Button)findViewById(R.id.btnFragments);
        btn_activities = (Button)findViewById(R.id.btnActivity);
        btn_google = (Button)findViewById(R.id.btnGoogle);
        btn_intent = (Button)findViewById(R.id.btnIntent);
        btn_database = (Button)findViewById(R.id.btnDatabase);

        // TODO: 7/10/2017 complete setting onclickListener
        btn_quickStart.setOnClickListener(this);
        btn_general.setOnClickListener(this);
        btn_fundamentals.setOnClickListener(this);
        btn_google.setOnClickListener(this);
        btn_activities.setOnClickListener(this);
        btn_fragments.setOnClickListener(this);
        btn_intent.setOnClickListener(this);
        btn_database.setOnClickListener(this);

    }
    // TODO: 7/10/2017 complete onclick
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(MainActivity.this, QuizActivity.class);
        switch (view.getId()) {

            //send info of diff categories to quiz activities;
            case R.id.btnGeneral:
                intent.putExtra(TAG_CATEGORY_SHOW, CATEGORY_01);
                intent.putExtra(TAG_CATEGORY_DATA, Question.CATE_O1_GENERAL);
                break;
            case R.id.btnFundamentals:
                intent.putExtra(TAG_CATEGORY_SHOW,CATEGORY_02);
                intent.putExtra(TAG_CATEGORY_DATA, Question.CATE_O2_FUNDAMENTAL);
                break;
            case R.id.btnGoogle:
                intent.putExtra(TAG_CATEGORY_SHOW,CATEGORY_03);
                intent.putExtra(TAG_CATEGORY_DATA, Question.CATE_O3_GOOGLE);
                break;
            case R.id.btnActivity:
                intent.putExtra(TAG_CATEGORY_SHOW,CATEGORY_04);
                intent.putExtra(TAG_CATEGORY_DATA, Question.CATE_O4_ACTIVITY);
                break;
            case R.id.btnFragments:
                intent.putExtra(TAG_CATEGORY_SHOW,CATEGORY_05);
                intent.putExtra(TAG_CATEGORY_DATA, Question.CATE_O5_FRAGMENT);
                break;
            case R.id.btnIntent:
                intent.putExtra(TAG_CATEGORY_SHOW,CATEGORY_06);
                intent.putExtra(TAG_CATEGORY_DATA, Question.CATE_O6_INTENT);
                break;
            case R.id.btnDatabase:
                intent.putExtra(TAG_CATEGORY_SHOW,CATEGORY_07);
                intent.putExtra(TAG_CATEGORY_DATA, Question.CATE_O7_DATABASE);
                break;
            case R.id.quickStartBtn:
                intent.putExtra(TAG_CATEGORY_SHOW, CATEGORY_RANDOM);
                intent.putExtra(TAG_CATEGORY_DATA, Question.CATE_RANDOM);
                break;
        }
        startActivity(intent);
    }

    //CRUD
    public Uri createQuestion(Question question) {

        ContentValues values = new ContentValues();

        values.put(DatabaseContract.QuestionEntry.KEY_QUESTION_ID, question.getQuestionID());
        values.put(DatabaseContract.QuestionEntry.QUESTION_CONTENT, question.getContent());
        values.put(DatabaseContract.QuestionEntry.QUESTION_CATEGORY, question.getCategory());
        values.put(DatabaseContract.QuestionEntry.QUESTION_DIFFICULTY, question.getDifficulty());
        values.put(DatabaseContract.QuestionEntry.QUESTION_TRUE_ANSWER, question.getTrueAnswer());
        String[] options = question.getAnswerOptions();
        if(options.length == 4) {
            values.put(DatabaseContract.QuestionEntry.QUESTION_ANSWER_OPTIONS_1, options[0]);
            values.put(DatabaseContract.QuestionEntry.QUESTION_ANSWER_OPTIONS_2, options[1]);
            values.put(DatabaseContract.QuestionEntry.QUESTION_ANSWER_OPTIONS_3, options[2]);
            values.put(DatabaseContract.QuestionEntry.QUESTION_ANSWER_OPTIONS_4, options[3]);
        }

        // insert row
        Uri id = cr.insert(QuestionProvider.CONTENT_URI,values);

        return id;
    }

    private void setDatabase(){
        //set up database
        db = new DatabaseHelper(getApplicationContext());
        db.cleanDatabase();
        cr = getContentResolver();
        String[] options1 = {"ViewGroup","Display", "Activity","None of the above"};
        Question q = new Question(1,"What is the name of the class which is inherited to create a user view ?",
                Question.EASY,Question.CATE_O1_GENERAL,options1,3);
        Log.d("DATABASE TESTING", createQuestion(q).toString());

        String[] options2 = {"Application","Manifest","Activity","Action"};
        q = new Question(2, "The root element of AndroidManifest.xml is:",
                Question.EASY,Question.CATE_O1_GENERAL,options2,2);
        createQuestion(q);

        String[] options3 = {"/assets","/src","/res/values","/res/layout"};
        q = new Question(3, "In which directory are the XML layout files stored in an android application:",
                Question.EASY,Question.CATE_O1_GENERAL,options3,4);
        createQuestion(q);

        String[] options4 = {"Relative Layout","Frame Layout","Linear Layout","Grid Layout"};
        q = new Question(4, "Name the layout where the positions of the children can be described in relation to each other or the parent:",
                Question.MED,Question.CATE_O1_GENERAL,options4,1);
        createQuestion(q);

        String[] options5 = {"onCreate()","findViewById()","setContentView","None of the above"};
        q = new Question(5, "Select the method used to access a view element of a layout resource in an activity:",
                Question.MED,Question.CATE_O1_GENERAL,options5,2);
        createQuestion(q);

        String[] options6 = {"R.style","X.style","Manifest.XML","Application"};
        q = new Question(6, "Android provides a few standard themes, listed in__________?",
                Question.HARD,Question.CATE_O1_GENERAL,options6,1);
        createQuestion(q);

        String[] options7 = {"Resources","Dalvik Executable","Both a and b","None of above\n"};
        q = new Question(7, "Which of the following(s) is/are component of APK file?",
                Question.HARD,Question.CATE_O1_GENERAL,options7,3);
        createQuestion(q);

        String[] options8 = {"Desktop Operating System","Programming Language","Mobile Operating System","Database"};
        q = new Question(8, "What is android?",
                Question.EASY,Question.CATE_O1_GENERAL,options8,3);
        createQuestion(q);

        String[] options9 = {"HTC Hero","Google gPhone","T-Mobile G1","Motorola Droid"};
        q = new Question(9, "What was the first phone released that ran the Android OS?",
                Question.EASY,Question.CATE_O1_GENERAL,options9,3);
        createQuestion(q);

        String[] options10 = {"Only Radio interface layer and alarm are in active mode","Switched off","Airplane mode","None of the Above"};
        q = new Question(10, "What is sleep mode in android?",
                Question.HARD,Question.CATE_O1_GENERAL,options10,1);
        createQuestion(q);
    }
}

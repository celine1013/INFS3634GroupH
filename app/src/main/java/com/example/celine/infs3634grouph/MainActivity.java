package com.example.celine.infs3634grouph;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import com.example.celine.infs3634grouph.dbHelper.DatabaseHelper;
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
    //public static final int CATEGORY_07 = R.string.category_07;

    // TODO: 7/10/2017 declare all widgets
    private Button btn_quickStart;
    private Button btn_general;
    private Button btn_fundamentals;
    private Button btn_google;
    private Button btn_activities;
    private Button btn_fragments;
    private Button btn_intent;
    //private Button btn_database;

    public static final String TAG_CATEGORY = "category";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set up database
        db = new DatabaseHelper(getApplicationContext());

        //for testing convenience: will clean up all data and re-load when restart the app
        db.cleanDatabase();
        Log.d("SETTING DATABASE", "Database rebuilt");

        //import sample data
        // TODO: 25/09/2017 add data into database
        List<Question> questions = new ArrayList<>();
        /*String[] str = getResources().getStringArray(R.array.questions);
        for(int i = 0; i < MAX_QUESTION; i++){
            Question q = new Question();
            if(i < MAX_CAT1){
                q = new Question(str[i], R.string.category_01, 1, );
            }else if(i<MAX_CAT2){
               q = new Question(str[i], R.string.category_02, 1);
            }else if(i<MAX_CAT3){
                q = new Question(str[i], R.string.category_03, 1);
            }else if(i<MAX_CAT4){
                q = new Question(str[i], R.string.category_04, 1);
            }else if(i<MAX_CAT5){
                q = new Question(str[i], R.string.category_05, 1);
            }
            questions.add(q);
        }*/
        db.preloadQuestions(questions);
        Log.v("SETTING DATABASE", "Data loading completed");

        // TODO: 7/10/2017 complete binding
        btn_quickStart = (Button)findViewById(R.id.quickStartBtn);
        btn_general = (Button)findViewById(R.id.btnGeneral);
        btn_fundamentals = (Button)findViewById(R.id.btnFundamentals);
        btn_fragments = (Button)findViewById(R.id.btnFragments);
        btn_activities = (Button)findViewById(R.id.btnActivity);
        btn_google = (Button)findViewById(R.id.btnGoogle);
        btn_intent = (Button)findViewById(R.id.btnIntent);
        //btn_database = (Button)findViewById(R.id.btnDatabase);

        // TODO: 7/10/2017 complete setting onclickListener
        btn_quickStart.setOnClickListener(this);
        btn_general.setOnClickListener(this);
        btn_fundamentals.setOnClickListener(this);
        btn_google.setOnClickListener(this);
        btn_activities.setOnClickListener(this);
        btn_fragments.setOnClickListener(this);
        btn_intent.setOnClickListener(this);
        //btn_batabase.setOnClickListener(this);

    }
    // TODO: 7/10/2017 complete onclick
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(MainActivity.this, QuizActivity.class);
        switch (view.getId()) {

            //send info of diff categories to quiz activities;
            case R.id.btnGeneral:
                intent.putExtra(TAG_CATEGORY, CATEGORY_01);
                break;
            case R.id.btnFundamentals:
                intent.putExtra(TAG_CATEGORY,CATEGORY_02);
                break;
            case R.id.btnGoogle:
                intent.putExtra(TAG_CATEGORY,CATEGORY_03);
                break;
            case R.id.btnActivity:
                intent.putExtra(TAG_CATEGORY,CATEGORY_04);
                break;
            case R.id.btnFragments:
                intent.putExtra(TAG_CATEGORY,CATEGORY_05);
                break;
            case R.id.btnIntent:
                intent.putExtra(TAG_CATEGORY,CATEGORY_06);
                break;
            //case R.id.btnDatabase:
            //    intent.putExtra(TAG_CATEGORY,CATEGORY_07);
            //    break;
            case R.id.quickStartBtn:
                intent.putExtra(TAG_CATEGORY, CATEGORY_RANDOM);
                break;
        }
        startActivity(intent);
    }
}

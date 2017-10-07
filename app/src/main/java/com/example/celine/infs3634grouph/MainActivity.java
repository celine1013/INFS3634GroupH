package com.example.celine.infs3634grouph;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import dbHelper.DatabaseHelper;
import model.Question;

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
   /* public static final int CATEGORY_ = 3;
    public static final int CATEGORY_ = 4;
    public static final int CATEGORY_ = 5;
    public static final int CATEGORY_ = 6;*/

    // TODO: 7/10/2017 declare all widgets
    private Button btn_quickStart;
    private ImageButton ib_cate01;


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
        ib_cate01 = (ImageButton)findViewById(R.id.category_1);

        // TODO: 7/10/2017 complete setting onclickListener
        btn_quickStart.setOnClickListener(this);
        ib_cate01.setOnClickListener(this);

    }
    // TODO: 7/10/2017 complete onclick
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(MainActivity.this, QuizActivity.class);
        switch (view.getId()) {

            //send info of diff categories to quiz activities;
            case R.id.category_1:
                intent.putExtra(TAG_CATEGORY, CATEGORY_01);
                break;
            case R.id.category_2:
                break;
            case R.id.category_3:
                break;
            case R.id.category_4:
                break;
            case R.id.category_5:
                break;
            case R.id.category_6:
                break;
            case R.id.quickStartBtn:
                intent.putExtra(TAG_CATEGORY, CATEGORY_RANDOM);
                break;
            default:
                startActivity(intent);
        }

    }
}

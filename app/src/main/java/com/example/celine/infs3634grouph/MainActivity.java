package com.example.celine.infs3634grouph;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import dbHelper.DatabaseHelper;
import model.Question;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper db;
    private static final int MAX_QUESTION = 50;
    private static final int MAX_CAT1 = 10;
    private static final int MAX_CAT2 = 20;
    private static final int MAX_CAT3 = 30;
    private static final int MAX_CAT4 = 40;
    private static final int MAX_CAT5 = 50;

    // TODO: 26/09/2017 declare widget fields

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
        // TODO: 25/09/2017 add data into strings.xml(question content + category list)
        List<Question> questions = new ArrayList<>();
        String[] str = getResources().getStringArray(R.array.questions);
        /*for(int i = 0; i < MAX_QUESTION; i++){
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


        // TODO: 26/09/2017 bind widgets

        // TODO: 26/09/2017 set btn onclick(start button)
        //onclick: start quiz activity, send data of category
    }
}

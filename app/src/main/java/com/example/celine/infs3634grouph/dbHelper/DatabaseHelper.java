package com.example.celine.infs3634grouph.dbHelper;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.example.celine.infs3634grouph.model.Question;



/**
 * Created by Celine on 25/09/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "com.example.celine.infs3634grouph_database";

    private ContentResolver cr;

    // Table Creation Statement
    private static final String CREATE_TABLE_QUESTION = "CREATE TABLE IF NOT EXISTS "
            + DatabaseContract.QuestionEntry.TABLE_QUESTION
            + "(" + DatabaseContract.QuestionEntry.KEY_QUESTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DatabaseContract.QuestionEntry.QUESTION_CONTENT + " TEXT,"
            + DatabaseContract.QuestionEntry.QUESTION_CATEGORY + " INTEGER,"
            + DatabaseContract.QuestionEntry.QUESTION_DIFFICULTY + " INTEGER,"
            + DatabaseContract.QuestionEntry.QUESTION_ANSWER_OPTIONS_1 + " TEXT,"
            + DatabaseContract.QuestionEntry.QUESTION_ANSWER_OPTIONS_2 + " TEXT,"
            + DatabaseContract.QuestionEntry.QUESTION_ANSWER_OPTIONS_3 + " TEXT,"
            + DatabaseContract.QuestionEntry.QUESTION_ANSWER_OPTIONS_4 + " TEXT,"
            + DatabaseContract.QuestionEntry.QUESTION_TRUE_ANSWER + " INTEGER" + ")";

    //Constructor


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        cr = context.getContentResolver();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_QUESTION);
        // TODO: 12/10/2017 preload data here
        Log.d("DATABASE SETTING", "TABLE CREATION COMPLETED");
    }

    /*
    public void addQuestions(){

        Question q1 = new Question(1,"What is the name of the class which is inherited to create a user" +
                " interface screen?",2,1,{"ViewGroup","Display", "Activity","None of the above"},3,10);
    }
    */

    //onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.QuestionEntry.TABLE_QUESTION);
        // create new tables
        onCreate(db);
    }

    public void cleanDatabase(){
        // on upgrade drop older tables
        Log.d("CLEAN", "cleaning database");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.QuestionEntry.TABLE_QUESTION);
        onCreate(db);
        Log.d("CLEAN", "DATABASE CLEANED");
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
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
        Uri question_id = cr.insert(QuestionProvider.CONTENT_URI,values);
        closeDB();
        return question_id;
    }


    public List<Question> getAllQuestions() {

        Cursor c = cr.query(QuestionProvider.CONTENT_URI, null, null, null, null);
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

        String selection = DatabaseContract.QuestionEntry.QUESTION_CATEGORY + "=" + category;
        Cursor c = cr.query(QuestionProvider.CONTENT_URI, null, selection, null, null);

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

    /*not yet needed*/
    //update

    //delete
}

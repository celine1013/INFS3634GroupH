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

    private static final String CREATE_TABLE_RECORD = "CREATE TABLE IF NOT EXISTS "
            + DatabaseContract.RecordEntry.TABLE_RECORD
            + "(" + DatabaseContract.RecordEntry.RECORD_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DatabaseContract.RecordEntry.RECORD_TIME + " TEXT,"
            + DatabaseContract.RecordEntry.RECORD_CATE + " INTEGER,"
            + DatabaseContract.RecordEntry.RECORD_DIFF + " INTEGER,"
            + DatabaseContract.RecordEntry.RECORD_SCORE+ " INTEGER,"
            + DatabaseContract.RecordEntry.RECORD_SPEED + " INTEGER" + ")";

    //Constructor


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        cr = context.getContentResolver();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_QUESTION);
        db.execSQL(CREATE_TABLE_RECORD);
        Log.i("DATABASE SETTING", "TABLE CREATION COMPLETED");
    }

    //onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.QuestionEntry.TABLE_QUESTION);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.RecordEntry.TABLE_RECORD);
        // create new tables
        onCreate(db);
    }

    public void cleanDatabase(){
        // on upgrade drop older tables
        Log.d("CLEAN", "cleaning database");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.QuestionEntry.TABLE_QUESTION);
        //db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.RecordEntry.TABLE_RECORD);
        onCreate(db);
        Log.d("CLEAN", "DATABASE CLEANED");
    }

    /*not yet needed*/
    //update

    //delete
}

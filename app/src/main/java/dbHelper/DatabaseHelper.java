package dbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import model.Question;

/**
 * Created by Celine on 25/09/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "com.example.celine.infs3634grouph_database";

    // Table names
    private static final String TABLE_QUESTION = "questions";

    // Column names
    private static final String KEY_QUESTION_ID = "question_id";
    private static final String QUESTION_CONTENT = "question_content";
    private static final String QUESTION_CATEGORY = "question_category";
    private static final String QUESTION_DIFFICULTY = "question_difficulty";
    private static final String QUESTION_ANSWER_OPTIONS_1 = "question_answer_options_1";
    private static final String QUESTION_ANSWER_OPTIONS_2 = "question_answer_options_2";
    private static final String QUESTION_ANSWER_OPTIONS_3 = "question_answer_options_3";
    private static final String QUESTION_ANSWER_OPTIONS_4 = "question_answer_options_4";
    private static final String QUESTION_TRUE_ANSWER = "question_true_answer";

    // Table Creation Statement
    private static final String CREATE_TABLE_QUESTION = "CREATE TABLE IF NOT EXISTS "
            + TABLE_QUESTION + "(" + KEY_QUESTION_ID + " INTEGER PRIMARY KEY," + QUESTION_CONTENT
            + " TEXT," + QUESTION_CATEGORY + " INTEGER," + QUESTION_DIFFICULTY + " INTEGER,"
            + QUESTION_ANSWER_OPTIONS_1 + " TEXT," + QUESTION_ANSWER_OPTIONS_2 + " TEXT,"
            + QUESTION_ANSWER_OPTIONS_3 + " TEXT,"  + QUESTION_ANSWER_OPTIONS_4 + " TEXT,"
            + QUESTION_TRUE_ANSWER + " INTEGER" + ")";

    //Constructor


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_QUESTION);
        Log.d("DATABASE SETTING", "TABLE CREATION COMPLETED");
    }

    //onUpgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);
        // create new tables
        onCreate(db);
    }

    public void cleanDatabase(){
        // on upgrade drop older tables
        Log.d("CLEAN", "cleaning database");
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTION);
        onCreate(db);
        Log.d("CLEAN", "DATABASE CLEANED");
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    //CRUD
    public long createQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_QUESTION_ID, question.getQuestionID());
        values.put(QUESTION_CONTENT, question.getContent());
        values.put(QUESTION_CATEGORY, question.getCategory());
        values.put(QUESTION_DIFFICULTY, question.getDifficulty());
        values.put(QUESTION_TRUE_ANSWER, question.getTrueAnswer());
        String[] options = question.getAnswerOptions();
        if(options.length == 4) {
            values.put(QUESTION_ANSWER_OPTIONS_1, options[0]);
            values.put(QUESTION_ANSWER_OPTIONS_2, options[1]);
            values.put(QUESTION_ANSWER_OPTIONS_3, options[2]);
            values.put(QUESTION_ANSWER_OPTIONS_4, options[3]);
        }

        // insert row
        long question_id = db.insert(TABLE_QUESTION, null, values);
        closeDB();
        return question_id;
    }

    public void preloadQuestions(List<Question> l) {
        SQLiteDatabase db = this.getWritableDatabase();

        for(Question question: l) {
            ContentValues values = new ContentValues();
            values.put(KEY_QUESTION_ID, question.getQuestionID());
            values.put(QUESTION_CONTENT, question.getContent());
            values.put(QUESTION_CATEGORY, question.getCategory());
            values.put(QUESTION_DIFFICULTY, question.getDifficulty());
            values.put(QUESTION_TRUE_ANSWER, question.getTrueAnswer());
            String[] options = question.getAnswerOptions();
            if(options.length == 4) {
                values.put(QUESTION_ANSWER_OPTIONS_1, options[0]);
                values.put(QUESTION_ANSWER_OPTIONS_2, options[1]);
                values.put(QUESTION_ANSWER_OPTIONS_3, options[2]);
                values.put(QUESTION_ANSWER_OPTIONS_4, options[3]);
            }

            // insert row
            long question_id = db.insert(TABLE_QUESTION, null, values);
        }
        closeDB();
        //return question_id;
    }

    public List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_QUESTION ;

        Log.e("DATABASE QUERIES", selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if(c != null) {
            if (c.moveToFirst()) {
                do {
                    Question q = new Question();
                    q.setQuestionID(c.getInt(c.getColumnIndex(KEY_QUESTION_ID)));
                    q.setContent(c.getString(c.getColumnIndex(QUESTION_CONTENT)));
                    q.setCategory(c.getInt(c.getColumnIndex(QUESTION_CATEGORY)));
                    q.setDifficulty(c.getInt(c.getColumnIndex(QUESTION_DIFFICULTY)));
                    q.setTrueAnswer(c.getInt(c.getColumnIndex(QUESTION_TRUE_ANSWER)));

                    String[] options = new String[4];
                    options[0] = c.getString(c.getColumnIndex(QUESTION_ANSWER_OPTIONS_1));
                    options[1] = c.getString(c.getColumnIndex(QUESTION_ANSWER_OPTIONS_2));
                    options[2] = c.getString(c.getColumnIndex(QUESTION_ANSWER_OPTIONS_3));
                    options[3] = c.getString(c.getColumnIndex(QUESTION_ANSWER_OPTIONS_4));
                    q.setAnswerOptions(options);
                    // adding to the list
                    questions.add(q);
                } while (c.moveToNext());
            }
        }
        c.close();
        db.close();
        return questions;
    }

    public List<Question> getQuestionsByCategory(int category) {
        List<Question> questions = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_QUESTION + " WHERE " + QUESTION_CATEGORY
                + " = " + category;

        Log.e("DATABASE QUERIES", selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if(c != null) {
            if (c.moveToFirst()) {
                do {
                    Question q = new Question();
                    q.setQuestionID(c.getInt(c.getColumnIndex(KEY_QUESTION_ID)));
                    q.setContent(c.getString(c.getColumnIndex(QUESTION_CONTENT)));
                    q.setCategory(c.getInt(c.getColumnIndex(QUESTION_CATEGORY)));
                    q.setDifficulty(c.getInt(c.getColumnIndex(QUESTION_DIFFICULTY)));
                    q.setTrueAnswer(c.getInt(c.getColumnIndex(QUESTION_TRUE_ANSWER)));

                    String[] options = new String[4];
                    options[0] = c.getString(c.getColumnIndex(QUESTION_ANSWER_OPTIONS_1));
                    options[1] = c.getString(c.getColumnIndex(QUESTION_ANSWER_OPTIONS_2));
                    options[2] = c.getString(c.getColumnIndex(QUESTION_ANSWER_OPTIONS_3));
                    options[3] = c.getString(c.getColumnIndex(QUESTION_ANSWER_OPTIONS_4));
                    q.setAnswerOptions(options);
                    // adding to the list
                    questions.add(q);
                } while (c.moveToNext());
            }
        }
        c.close();
        db.close();
        return questions;
    }

    /*not yet needed*/
    //update

    //delete
}

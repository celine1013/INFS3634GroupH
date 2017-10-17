package com.example.celine.infs3634grouph.dbHelper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.celine.infs3634grouph.MainActivity;
import com.example.celine.infs3634grouph.QuizActivity;
import com.example.celine.infs3634grouph.dbHelper.DatabaseContract;
import com.example.celine.infs3634grouph.model.Question;

import java.util.IllegalFormatCodePointException;
import java.util.List;

/**
 * Created by Celine on 12/10/2017.
 */

public class QuestionProvider extends ContentProvider {
    private static final String AUTHORITY = "com.example.celine.infs3634grouph.questionprovider";
    private static final String BASE_PATH = "questions";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );

    // Constant to identify the requested operation
    private static final int QUESTION = 1;
    private static final int QUESTION_ID = 2;
    private static final int MAX_QUESTION_NUM = 10;

    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, QUESTION);
        uriMatcher.addURI(AUTHORITY, BASE_PATH +  "/#", QUESTION_ID);
    }

    private SQLiteDatabase database;
    @Override
    public boolean onCreate() {
        DatabaseHelper helper = new DatabaseHelper(getContext());
        database = helper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return database.query(DatabaseContract.QuestionEntry.TABLE_QUESTION, DatabaseContract.QuestionEntry.QUESTION_ALL_COLUMNS,
                selection, null, null, null, null, " RANDOM() LIMIT "+MAX_QUESTION_NUM);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        int uriType = uriMatcher.match(uri);
        if(uriType != QUESTION){
            throw new IllegalArgumentException("Uknown uri: " + uri);
        }
        long id = database.insert(DatabaseContract.QuestionEntry.TABLE_QUESTION,
                null, contentValues);
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return database.delete(DatabaseContract.QuestionEntry.TABLE_QUESTION, selection, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        return database.update(DatabaseContract.QuestionEntry.TABLE_QUESTION, contentValues,
                selection, selectionArgs);
    }


}

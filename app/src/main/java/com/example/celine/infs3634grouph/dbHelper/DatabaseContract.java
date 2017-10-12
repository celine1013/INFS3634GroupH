package com.example.celine.infs3634grouph.dbHelper;

import android.provider.BaseColumns;

/**
 * Created by Celine on 12/10/2017.
 */

public class DatabaseContract {
    private DatabaseContract(){
    }

    public static final class QuestionEntry implements BaseColumns{
        // Table names
        public static final String TABLE_QUESTION = "questions";

        // Column names
        public static final String KEY_QUESTION_ID = "_id";
        public static final String QUESTION_CONTENT = "question_content";
        public static final String QUESTION_CATEGORY = "question_category";
        public static final String QUESTION_DIFFICULTY = "question_difficulty";
        public static final String QUESTION_ANSWER_OPTIONS_1 = "question_answer_options_1";
        public static final String QUESTION_ANSWER_OPTIONS_2 = "question_answer_options_2";
        public static final String QUESTION_ANSWER_OPTIONS_3 = "question_answer_options_3";
        public static final String QUESTION_ANSWER_OPTIONS_4 = "question_answer_options_4";
        public static final String QUESTION_TRUE_ANSWER = "question_true_answer";

        public static final String[] QUESTION_ALL_COLUMNS =
                {KEY_QUESTION_ID, QUESTION_CONTENT, QUESTION_CATEGORY,QUESTION_DIFFICULTY,
                QUESTION_ANSWER_OPTIONS_1,QUESTION_ANSWER_OPTIONS_2,QUESTION_ANSWER_OPTIONS_3,QUESTION_ANSWER_OPTIONS_4,
                QUESTION_TRUE_ANSWER};
    }
}

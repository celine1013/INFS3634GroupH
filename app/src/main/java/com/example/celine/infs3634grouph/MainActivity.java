package com.example.celine.infs3634grouph;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import com.example.celine.infs3634grouph.dbHelper.DatabaseContract;
import com.example.celine.infs3634grouph.dbHelper.DatabaseHelper;
import com.example.celine.infs3634grouph.dbHelper.QuestionProvider;
import com.example.celine.infs3634grouph.model.Question;

public class MainActivity extends AppCompatActivity implements CustomDialogFragment.NoticeDialogListener, View.OnClickListener {

    DatabaseHelper db;
    private static final int MAX_QUESTION = 50;
    private static final int MAX_CAT1 = 10;
    private static final int MAX_CAT2 = 20;
    private static final int MAX_CAT3 = 30;
    private static final int MAX_CAT4 = 40;
    private static final int MAX_CAT5 = 50;

    public static final String TAG_DIFF = "tag_diff";

    //declare integer used in building intents
    public static final int CATEGORY_RANDOM = R.string.category_random;
    public static final int CATEGORY_01 = R.string.category_01;
    public static final int CATEGORY_02 = R.string.category_02;
    public static final int CATEGORY_03 = R.string.category_03;
    public static final int CATEGORY_04 = R.string.category_04;
    public static final int CATEGORY_05 = R.string.category_05;
    public static final int CATEGORY_06 = R.string.category_06;
    public static final int CATEGORY_07 = R.string.category_07;

    private int speed;
    public static final String TAG_SPEED = "tag_speed";
    public static final int SPEED_SLOW = 0;
    public static final int SPEED_MEDIUM = 1;
    public static final int SPEED_FAST = 2;

    private int difficulty;

    //declare all widgets
    private Button btn_quickStart;
    private Button btn_history;
    private CardView btn_general;
    private CardView btn_fundamentals;
    private CardView btn_google;
    private CardView btn_activities;
    private CardView btn_fragments;
    private CardView btn_intent;
    private CardView btn_database;

    public static final String TAG_CATEGORY_SHOW = "category_str";
    public static final String TAG_CATEGORY_DATA = "category_dat";
    private ContentResolver cr;

    public MediaPlayer mp;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);
        setDatabase();
        intent = new Intent(MainActivity.this, QuizActivity.class);

        btn_quickStart = (Button) findViewById(R.id.quickStartBtn);
        btn_history = (Button)findViewById(R.id.btn_his);
        btn_general = (CardView)findViewById(R.id.btn_general);
        btn_fundamentals = (CardView)findViewById(R.id.btn_fundamental);
        btn_fragments = (CardView)findViewById(R.id.btn_fragments);
        btn_activities = (CardView)findViewById(R.id.btn_activities);
        btn_google = (CardView)findViewById(R.id.btn_google);
        btn_intent = (CardView)findViewById(R.id.btn_intents);
        btn_database = (CardView)findViewById(R.id.btn_database);


        btn_quickStart.setOnClickListener(this);
        btn_general.setOnClickListener(this);
        btn_fundamentals.setOnClickListener(this);
        btn_google.setOnClickListener(this);
        btn_activities.setOnClickListener(this);
        btn_fragments.setOnClickListener(this);
        btn_intent.setOnClickListener(this);
        btn_database.setOnClickListener(this);

        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, PastRecordActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onResume(){
        super.onResume();
        mp = MediaPlayer.create(MainActivity.this, R.raw.bgm01);
        mp.setLooping(true); // Set looping
        mp.setVolume(1.0f, 1.0f);
        mp.start();
    }
    @Override
    public void onPause(){
        super.onPause();
        mp.stop();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            //send info of diff categories to quiz activities;
            case R.id.btn_general:
                intent.putExtra(TAG_CATEGORY_SHOW, CATEGORY_01);
                intent.putExtra(TAG_CATEGORY_DATA, Question.CATE_O1_GENERAL);
                break;
            case R.id.btn_fundamental:
                intent.putExtra(TAG_CATEGORY_SHOW,CATEGORY_02);
                intent.putExtra(TAG_CATEGORY_DATA, Question.CATE_O2_FUNDAMENTAL);
                break;
            case R.id.btn_google:
                intent.putExtra(TAG_CATEGORY_SHOW,CATEGORY_03);
                intent.putExtra(TAG_CATEGORY_DATA, Question.CATE_O3_GOOGLE);
                break;
            case R.id.btn_activities:
                intent.putExtra(TAG_CATEGORY_SHOW,CATEGORY_04);
                intent.putExtra(TAG_CATEGORY_DATA, Question.CATE_O4_ACTIVITY);
                break;
            case R.id.btn_fragments:
                intent.putExtra(TAG_CATEGORY_SHOW,CATEGORY_05);
                intent.putExtra(TAG_CATEGORY_DATA, Question.CATE_O5_FRAGMENT);
                break;
            case R.id.btn_intents:
                intent.putExtra(TAG_CATEGORY_SHOW,CATEGORY_06);
                intent.putExtra(TAG_CATEGORY_DATA, Question.CATE_O6_INTENT);
                break;
            case R.id.btn_database:
                intent.putExtra(TAG_CATEGORY_SHOW,CATEGORY_07);
                intent.putExtra(TAG_CATEGORY_DATA, Question.CATE_O7_DATABASE);
                break;
            case R.id.quickStartBtn:
                intent.putExtra(TAG_CATEGORY_SHOW, CATEGORY_RANDOM);
                intent.putExtra(TAG_CATEGORY_DATA, Question.CATE_RANDOM);
                break;
        }
        CustomDialogFragment dialog = new CustomDialogFragment();
        dialog.show(getSupportFragmentManager(),"CUSTOM QUIZ FRAGMENT");
    }
    @Override
    public void onDialogPositiveClick(CustomDialogFragment dialog) {
        speed = dialog.sp_speed.getSelectedItemPosition();
        difficulty = dialog.sp_diff.getSelectedItemPosition();
        Log.d("SPEED", String.valueOf(speed));
        Log.d("DIFF", String.valueOf(difficulty));
        intent.putExtra(TAG_SPEED, speed);
        intent.putExtra(TAG_DIFF, difficulty);
        startActivity(intent);
    }

    @Override
    public void onDialogNegativeClick(CustomDialogFragment dialog) {
        dialog.getDialog().cancel();
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

        //General Questions
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

        //Fundamentals Questions
        String[] options11 = {"Cupcake","Gingerbread","Honeycomb","Muffin"};
        q = new Question(11, "Which one of the following is NOT a version of Android?",
                Question.EASY,Question.CATE_O2_FUNDAMENTAL,options11,4);
        createQuestion(q);

        String[] options12 = {"Linux","Windows","Java","XML"};
        q = new Question(12, "What OS is used at the base of the Android stack?",
                Question.EASY,Question.CATE_O2_FUNDAMENTAL,options12,1);
        createQuestion(q);

        String[] options13 = {"2007","2005","2008","2010"};
        q = new Question(13, "When did Google purchase Android?",
                Question.MED,Question.CATE_O2_FUNDAMENTAL,options13,2);
        createQuestion(q);

        String[] options14 = {"KitKat","Lollipop","Marshmallow","Jelly Bean"};
        q = new Question(14, "Android 6.0 is also referred to as what?",
                Question.MED,Question.CATE_O2_FUNDAMENTAL,options14,3);
        createQuestion(q);

        String[] options15 = {"It will react on broadcast announcements","It will do background functionalities as services","It will pass the data between activities","None of the Above"};
        q = new Question(15, "Which of the following defines a structured interface to application data?",
                Question.HARD,Question.CATE_O2_FUNDAMENTAL,options15,1);
        createQuestion(q);

        String[] options16 = {"Only Radio interface layer and alarm are in active mode","Switched off","Airplane mode","None of the Above"};
        q = new Question(16, "What is Broadcast Receiver in android?",
                Question.MED,Question.CATE_O2_FUNDAMENTAL,options16,1);
        createQuestion(q);

        String[] options17 = {"C","C++","VC++","Java"};
        q = new Question(17, "Android is based on which language?",
                Question.EASY,Question.CATE_O2_FUNDAMENTAL,options17,4);
        createQuestion(q);

        String[] options18 = {"commitUpdates()","updates()","commit()","None of these"};
        q = new Question(18, "To update contents of content provider using curser and commit you need to call________?",
                Question.MED,Question.CATE_O2_FUNDAMENTAL,options18,1);
        createQuestion(q);

        String[] options19 = {"Eclipse","Java and XML","Java and SQL","Java and PI/sql"};
        q = new Question(19, "For creating user interface in Android, you have to use?",
                Question.EASY,Question.CATE_O2_FUNDAMENTAL,options19,2);
        createQuestion(q);

        String[] options20 = {"Services","Simulator","Emulator","None of these"};
        q = new Question(20, "Which of the following don’t have any UI component and run as a background process?",
                Question.HARD,Question.CATE_O2_FUNDAMENTAL,options20,1);
        createQuestion(q);

        //Google Questions
        String[] options21 = {"Music","Books","Apps","All of the above"};
        q = new Question(21, "The Google Play store is a digital media store that offers which of the following?",
                Question.EASY,Question.CATE_O3_GOOGLE,options21,4);
        createQuestion(q);

        String[] options22 = {"Android Store","Android Market","The App Store","Google Marketplace"};
        q = new Question(22, "The Google Play store was originally referred to as what?",
                Question.EASY,Question.CATE_O3_GOOGLE,options22,2);
        createQuestion(q);

        String[] options23 = {"Google Play Console","Google Play Upload","Google Play Store","Google Play AppStore"};
        q = new Question(23, "Which service manages all phases of publishing Android apps onto the Google Play Store?",
                Question.MED,Question.CATE_O3_GOOGLE,options23,1);
        createQuestion(q);

        String[] options24 = {"Community Reviews","App License Verification","App Security Scanning","All of the above"};
        q = new Question(24, "Which of the following does the Google Play store also provide:",
                Question.MED,Question.CATE_O3_GOOGLE,options24,4);
        createQuestion(q);

        String[] options25 = {"PlayApiClient","AppStoreClient","GoogleApiClient","ServiceClient"};
        q = new Question(25, "To connect to Google Play services, which interface must you use?",
                Question.MED,Question.CATE_O3_GOOGLE,options25,3);
        createQuestion(q);

        String[] options26 = {"Application framework","Low-level Linux modules","Native libraries","All of these answers"};
        q = new Question(26, "What part of the Android platform is open source?",
                Question.HARD,Question.CATE_O3_GOOGLE,options26,4);
        createQuestion(q);

        String[] options27 = {"To allow them to advertise more","To corner the mobile device application market for licensing purposes",
                "To directly compete with the iPhone","To level the playing field for mobile devices"};
        q = new Question(27, "What was Google’s main business motivation for supporting Android?",
                Question.HARD,Question.CATE_O3_GOOGLE,options27,1);
        createQuestion(q);

        String[] options28 = {"Sprint Evo 4G","Samsung Galaxy","Motorola Droid X","Nokia N8"};
        q = new Question(28, "Which smartphone does not run on Android:",
                Question.EASY,Question.CATE_O3_GOOGLE,options28,4);
        createQuestion(q);

        String[] options29 = {"BlackBerry","Sidekick","iPhone","Palm Pilot"};
        q = new Question(29, "What other popular device did Android co-founder Andy Rubin help develop?",
                Question.HARD,Question.CATE_O3_GOOGLE,options29,2);
        createQuestion(q);

        String[] options30 = {"Cisco","HP","Samsung","Motorola"};
        q = new Question(30, "Which vendor has no plans for an Android-based tablet?",
                Question.EASY,Question.CATE_O3_GOOGLE,options30,2);
        createQuestion(q);

        //Activity Questions
        String[] options31 = {"onCreate(), onStart(), onResume()","onCreate(), onResume(), onStart()",
                "onCreate(), onMain(), on Start()","onCreate(), OnStart(), onStop()"};
        q = new Question(31, "What is the correct order of methods that are called in launching an activity:",
                Question.EASY,Question.CATE_O4_ACTIVITY,options31,1);
        createQuestion(q);

        String[] options32 = {"onPause()","onStop()","onVisible()","onDestory()"};
        q = new Question(32, "If the activity becomes completely invisible, the system calls which methods:",
                Question.EASY,Question.CATE_O4_ACTIVITY,options32,2);
        createQuestion(q);

        String[] options33 = {"onStop()","onPause()","onDestory()","onRestart()"};
        q = new Question(33, "If an activity is finishing, the system calls which method:",
                Question.MED,Question.CATE_O4_ACTIVITY,options33,3);
        createQuestion(q);

        String[] options34 = {"onStop()","onPause()","onStart()","onDestory()"};
        q = new Question(34, "The foreground lifetime of an activity happens between the call to onResume() " +
                "and the call to __________. During this time, the activity is in front of all other activities" +
                "on screen and has user input focus.",
                Question.HARD,Question.CATE_O4_ACTIVITY,options34,2);
        createQuestion(q);

        String[] options35 = {"onSaveActivity()","onSaveInstanceState()","onRestoreInstanceState()","onSaveBundle()"};
        q = new Question(35, "you can ensure that important information about the activity state is " +
                "preserved by implementing an additional callback method called: ",
                Question.HARD,Question.CATE_O4_ACTIVITY,options35,2);
        createQuestion(q);

        String[] options36 = {"Not possible","Wrong question","Yes, it is possible","None of the above"};
        q = new Question(36, "Is it possible to have an activity without UI to perform action/actions?",
                Question.MED,Question.CATE_O4_ACTIVITY,options36,3);
        createQuestion(q);

        String[] options37 = {"No, we can write the program without writing onCreate() and onStart()",
                "Yes, we should call onCreate() and onStart() to write the program","At least we need to call onCreate() once","None of the above"};
        q = new Question(37, "Is it mandatory to call onCreate() and onStart() in android?",
                Question.EASY,Question.CATE_O4_ACTIVITY,options37,1);
        createQuestion(q);

        String[] options38 = {"getAction()","getData()","getIntent()","getExtra()"};
        q = new Question(38, "An intent can also contain additional data based on an instance of the Bundle class which can be retrieved from the intent using which method:",
                Question.MED,Question.CATE_O4_ACTIVITY,options38,4);
        createQuestion(q);

        String[] options39 = {"onAnchor()","onCreateFragment()","onAttach()","onConnect()"};
        q = new Question(39, "onCreate of activity adds the fragment and in this moment __________ is called:",
                Question.EASY,Question.CATE_O4_ACTIVITY,options39,3);
        createQuestion(q);

        String[] options40 = {"Binder is responsible to manage the thread while using aidl in android",
                "Binder is responsible for marshalling and unmarshalling of the data","A & B","Binder is a kind of interface"};
        q = new Question(40, "What are the functionalities of Binder services in android?",
                Question.HARD,Question.CATE_O4_ACTIVITY,options40,3);
        createQuestion(q);

        // Fragment Questions
        String[] options41 = {"Controller","Model","View","None of the above"};
        q = new Question(41, "A fragment is a ________ object:",
                Question.EASY,Question.CATE_O5_FRAGMENT,options41,1);
        createQuestion(q);

        String[] options42 = {"UI","Resource","Manager","View"};
        q = new Question(42, "A ___________ fragment has a view of its own that is inflated from a layout file.",
                Question.EASY,Question.CATE_O5_FRAGMENT,options42,1);
        createQuestion(q);

        String[] options43 = {"The Operating System","Gradle","Manifest.xml","The Hosting Activity"};
        q = new Question(43, "The fragment lifecycle methods are called by:",
                Question.EASY,Question.CATE_O5_FRAGMENT,options43,4);
        createQuestion(q);

        String[] options44 = {"onAnchor()","onCreateFragment()","onAttach()","onConnect()"};
        q = new Question(44, "onCreate of activity adds the fragment and in this moment __________ is called:",
                Question.MED,Question.CATE_O5_FRAGMENT,options44,3);
        createQuestion(q);

        String[] options45 = {"SupportedFragment","FragmentActivity","AppCompatFragment","Fragment"};
        q = new Question(45, "The Support Library (support-v4) includes an Activity subclass that supports the implementation of fragments. It is called:",
                Question.HARD,Question.CATE_O5_FRAGMENT,options45,2);
        createQuestion(q);

        String[] options46 = {"Android:padding ","Android:digits","Android:capitalize ","Android:autoText"};
        q = new Question(46, "If you want to increase the whitespace between widgets, you will need to use the ____________ property?",
                Question.EASY,Question.CATE_O5_FRAGMENT,options46,1);
        createQuestion(q);

        String[] options47 = {"Activity","Services","Boardcast Receiver","Content Provider"};
        q = new Question(47, "_______ represents a single user interface screen. Select one:",
                Question.MED,Question.CATE_O5_FRAGMENT,options47,1);
        createQuestion(q);

        String[] options48 = {"Applications are distributed by multiple vendors with the exact same policies on applications.",
                "Applications are distributed by Apple App Store only",
                "Applications are distributed by multiple vendors with different policies on applications",
                "Applications are distributed by the Android Market only"};
        q = new Question(48, "What is a key difference with the distribution of apps for Android based devices " +
                "than other mobile device platform applications?",
                Question.EASY,Question.CATE_O5_FRAGMENT,options48,3);
        createQuestion(q);

        String[] options49 = {"Portability","Security","Networking","All of these"};
        q = new Question(49, "Android is based on Linux for the following reason?",
                Question.HARD,Question.CATE_O5_FRAGMENT,options49,4);
        createQuestion(q);

        String[] options50 = {"Collection of views and other child views","Base class of building blocks","Layouts","None of the above"};
        q = new Question(50, "What is android view group?",
                Question.MED,Question.CATE_O5_FRAGMENT,options50,1);
        createQuestion(q);

        // Intent Questions
        String[] options51 = {"Object","SstartActivity","Intent","None of the above"};
        q = new Question(51, "If you want to navigate from one activity to another then android provides you which class?",
                Question.EASY,Question.CATE_O6_INTENT,options51,3);
        createQuestion(q);

        String[] options52 = {"Intent intent=new Intent (this, ActivityTwo.class);\n" +
                "startActivity(intent);\n","startActivity(new Intent(this, ActivityTwo.class));",
                "Option A and B are both correct.","None of the above"};
        q = new Question(52, "Suppose that there are two activities in an application named ActivityOne and ActivityTwo." +
                " You want to invoke ActivityTwo from ActivityOne. What code you will write?",
                Question.EASY,Question.CATE_O6_INTENT,options52,3);
        createQuestion(q);

        String[] options53 = {"getAction()","getData()","getIntent()","getExtra()"};
        q = new Question(53, "An intent can also contain additional data based on an instance of the Bundle " +
                "class which can be retrieved from the intent using which method:",
                Question.MED,Question.CATE_O6_INTENT,options53,4);
        createQuestion(q);

        String[] options54 = {"ACTION_EMAIL","ACTION_SEND","ACTION_INSERT","ACTION_LAUNCH"};
        q = new Question(54, "An Activity that needs to launch an email client and sends an email using your Android device uses which action:",
                Question.MED,Question.CATE_O6_INTENT,options54,2);
        createQuestion(q);

        String[] options55 = {"Intent intent=new Intent (this, SecondActivity.class);\n" +
                "intent.putExtra(\"name\", “unsw.com”);\n" +
                "startActivity(intent);\n","Intent intent=new Intent (this, SecondActivity.class);\n" +
                "intent.putExtra( “unsw.com”);\n" +
                "startActivity(intent);\n","Intent intent=new Intent ();\n" +
                "intent.putExtra(\"name\", “unsw.com”);\n" +
                "startActivity(intent);\n","None of the above"};
        q = new Question(55, "Suppose that there are two activities in an application named FirstActivity and SecondActivity. You want to send website name" +
                " from ActivityOne to ActivityTwo. What code you will write? Suppose that website name is “unsw.com”",
                Question.HARD,Question.CATE_O6_INTENT,options55,1);
        createQuestion(q);

        String[] options56 = {"finish()","getGPSStatus()","onProviderDisable()","getGPS()"};
        q = new Question(56, "Which method is used to find GPS enabled or disabled pro-grammatically in android?",
                Question.EASY,Question.CATE_O6_INTENT,options56,3);
        createQuestion(q);

        String[] options57 = {"To create an activity","To create a graphical window for subclass","It allows the developers to write the program","None of the above"};
        q = new Question(57, "What is the purpose of super.onCreate() in android?",
                Question.EASY,Question.CATE_O6_INTENT,options57,2);
        createQuestion(q);

        String[] options58 = {"Implicit intent","Explicit intent","A and B","None of these"};
        q = new Question(58, "In ___________, sender specifies type of receiver.",
                Question.EASY,Question.CATE_O6_INTENT,options58,1);
        createQuestion(q);

        String[] options59 = {"Only one","Two","AsyncTask doesn't have tread","None of the above"};
        q = new Question(59, "How many threads are there in asyncTask in android?",
                Question.MED,Question.CATE_O6_INTENT,options59,1);
        createQuestion(q);

        String[] options60 = {"Object","SstartActivity","Intent","None of the above"};
        q = new Question(60, "If you want to navigate from one activity to another then android provides you which class?",
                Question.HARD,Question.CATE_O6_INTENT,options60,3);
        createQuestion(q);

        //Database Questions
        String[] options61 = {"Content values are key pair values, which are updated or inserted in the database",
                "Cursor is used to store the temporary result","A & B","Content values are used to share the data."};
        q = new Question(61, "What is the difference between content values and cursor in android SQlite?",
                Question.EASY,Question.CATE_O7_DATABASE,options61,3);
        createQuestion(q);

        String[] options62 = {"com.json","in.json","com.android.JSON","Org.json"};
        q = new Question(62, "What is the package name of JSON?",
                Question.MED,Question.CATE_O7_DATABASE,options62,4);
        createQuestion(q);

        String[] options63 = {"Chrome","Firefox","Open-source Webkit","Opera"};
        q = new Question(63, "Web browser available in android is based on",
                Question.EASY,Question.CATE_O7_DATABASE,options63,3);
        createQuestion(q);

        String[] options64 = {"Multitasking","Bluetooth","Video calling","All of the above"};
        q = new Question(64, "Android supports which features?",
                Question.MED,Question.CATE_O7_DATABASE,options64,4);
        createQuestion(q);

        String[] options65 = {"Dalvik","Webkit","SQLite","OpenGL"};
        q = new Question(65, "Which among these are NOT a part of Android’s native libraries?",
                Question.HARD,Question.CATE_O7_DATABASE,options65,1);
        createQuestion(q);

        String[] options66 = {"count()","sum()","add()","length()"};
        q = new Question(66, "How to find the JSON element length in android JSON?",
                Question.EASY,Question.CATE_O7_DATABASE,options66,4);
        createQuestion(q);

        String[] options67 = {"Yes, a user can save all database updates in onStop()","No, a user can save in onSavedInstance()",
                "No, a user can save in a Bundle()","No, In some situations, a user can't reach onStop()"};
        q = new Question(67, "Can a user save all database updates in onStop ()?",
                Question.EASY,Question.CATE_O7_DATABASE,options67,4);
        createQuestion(q);

        String[] options68= {"Shared Preferences","Cursor","SQlite database","Not possible"};
        q = new Question(68, "How to store heavy structured data in android?",
                Question.EASY,Question.CATE_O7_DATABASE,options68,3);
        createQuestion(q);

        String[] options69 = {"Permanent data","Secure data","Temporary data","Logical data"};
        q = new Question(69, "What is transient data in android?",
                Question.EASY,Question.CATE_O7_DATABASE,options69,4);
        createQuestion(q);

        String[] options70 = {"When the application is not responding ANR will occur","Dialog box is called as ANR.",
                "When Android forcefully kills an application, it is called ANR","None of the above"};
        q = new Question(70, "What is ANR in android?",
                Question.EASY,Question.CATE_O7_DATABASE,options70,1);
        createQuestion(q);
    }
}

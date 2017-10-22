package com.example.celine.infs3634grouph;

import android.content.ContentResolver;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.celine.infs3634grouph.dbHelper.DatabaseContract;
import com.example.celine.infs3634grouph.dbHelper.QuestionProvider;
import com.example.celine.infs3634grouph.dbHelper.RecordProvider;
import com.example.celine.infs3634grouph.model.Question;
import com.example.celine.infs3634grouph.model.Record;

import java.util.ArrayList;
import java.util.List;

public class PastRecordActivity extends AppCompatActivity {
    private RecyclerView rv_history;
    private ContentResolver cr;

    private List<Record> records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_record);
        rv_history = findViewById(R.id.rv_history);
        cr = getContentResolver();
        // TODO: 22/10/2017 set adapter
        rv_history.setLayoutManager(new LinearLayoutManager(this));
        records = getAllRecords();
        rv_history.setAdapter(new RecordAdapter(this,records));

    }

    public List<Record> getAllRecords() {

        // TODO: 22/10/2017 query order by score
        String selection = "";
        Cursor c = cr.query(RecordProvider.CONTENT_URI, DatabaseContract.RecordEntry.RECORD_ALL_COLUMNS, selection, null, null);
        List<Record> records = new ArrayList<>();
        // looping through all rows and adding to list
        if(c != null) {
            if (c.moveToFirst()) {
                do {
                    Record r = new Record();
                    r.setCategory(c.getInt(c.getColumnIndex(DatabaseContract.RecordEntry.RECORD_CATE)));
                    r.setDifficulty(c.getInt(c.getColumnIndex(DatabaseContract.RecordEntry.RECORD_DIFF)));
                    r.setSpeed(c.getInt(c.getColumnIndex(DatabaseContract.RecordEntry.RECORD_SPEED)));
                    r.setScore(c.getInt(c.getColumnIndex(DatabaseContract.RecordEntry.RECORD_SCORE)));
                    r.setDate(c.getString(c.getColumnIndex(DatabaseContract.RecordEntry.RECORD_TIME)));
                    // adding to the list
                    records.add(r);
                } while (c.moveToNext());
            }
        }
        c.close();
        return records;
    }
}

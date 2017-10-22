package com.example.celine.infs3634grouph;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

public class PastRecordActivity extends AppCompatActivity {
    private RecyclerView rv_history;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_record);
        rv_history = findViewById(R.id.rv_history);

    }
}

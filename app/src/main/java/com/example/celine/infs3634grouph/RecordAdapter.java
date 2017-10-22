package com.example.celine.infs3634grouph;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.celine.infs3634grouph.model.Record;
import java.util.List;

/**
 * Edited by Celine on 12/10/2017.
 * Refer to : lynda video: https://www.lynda.com/Android-tutorials/Display-data-RecyclerView/486757/555452-4.html?org=unsw.edu.au
 */

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {
    private List<Record> mItems;
    private Context mContext;

    public RecordAdapter(Context context, List<Record> records) {
        this.mContext = context;
        this.mItems = records;
    }

    @Override
    public RecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.record_item, parent, false);
        RecordViewHolder viewHolder = new RecordViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecordViewHolder holder, int position) {
        final Record record = mItems.get(position);

        holder.tv_cate.setText(mContext.getResources().getString(record.getCategory()));
        holder.tv_diff.setText(record.getSDifficulty());
        String str = record.getSSpeed()+ " Speed";
        holder.tv_speed.setText(str);
        holder.tv_score.setText(String.valueOf(record.getScore()));
        holder.tv_time.setText(record.getDate());
    }



    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class RecordViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_cate;
        public TextView tv_diff;
        public TextView tv_speed;
        public TextView tv_score;
        public TextView tv_time;

        public RecordViewHolder(View itemView) {
            super(itemView);
            tv_cate = (TextView) itemView.findViewById(R.id.tv_cate);
            tv_diff = (TextView) itemView.findViewById(R.id.tv_diff);
            tv_speed = (TextView) itemView.findViewById(R.id.tv_speed);
            tv_score = (TextView) itemView.findViewById(R.id.tv_score);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }
}

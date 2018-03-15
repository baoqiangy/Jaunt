package csc445.missouriwestern.edu.jaunt.fragments.hours;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import csc445.missouriwestern.edu.jaunt.Globals;
import csc445.missouriwestern.edu.jaunt.HoursEditActivity;
import csc445.missouriwestern.edu.jaunt.R;

/**
 * Created by byan on 1/25/2018.
 */

public class HoursRecyclerViewAdapter extends RecyclerView.Adapter<HoursRecyclerViewAdapter.HoursRecordHolder> {

    private Context context;
    private List<HoursRecord> records;
    private int selected_position = 0;
    private Color highlightColor;
    private Boolean clearBackGround;

    public HoursRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public HoursRecyclerViewAdapter(Context context, List<HoursRecord> records) {
        this.context = context;
        this.records = records;
        this.clearBackGround = true;
    }

    @Override
    public HoursRecordHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_day_hour, parent, false);
        HoursRecordHolder hoursRecordHolder = new HoursRecordHolder(view);
        return hoursRecordHolder;
    }

    @Override
    public void onBindViewHolder(HoursRecordHolder holder, int position) {
        HoursRecord record = records.get(position);
        holder.dayTextView.setText(record.getDayStr());
        holder.rangeTextView.setText(record.getRangesStr());
        holder.moreVertImageView.setOnClickListener(new OnHoursRecordOverflowSelectedListener(context, record, holder));
        Log.d("HoursViewAdapter", "onBindViewHolder selected_position -  " + selected_position);
        if(!clearBackGround) {
            holder.itemView.setBackgroundColor(selected_position == position ? ContextCompat.getColor(this.context, R.color.colorFocus) : Color.TRANSPARENT);
        }else{
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public class HoursRecordHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView dayTextView;
        private TextView rangeTextView;
        private ImageView moreVertImageView;

        public HoursRecordHolder(View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.hours_day);
            rangeTextView = itemView.findViewById(R.id.hours_hours);
            moreVertImageView = itemView.findViewById(R.id.hours_record_more_vert);
            itemView.setOnClickListener(this);
        }

        /**
         * Borrowed from https://stackoverflow.com/questions/27194044/how-to-properly-highlight-selected-item-on-recyclerview
         * */
        @Override
        public void onClick(View v) {
            // Below line is just like a safety check, because sometimes holder could be null,
            // in that case, getAdapterPosition() will return RecyclerView.NO_POSITION
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;
            clearBackGround = false;
            // Updating old as well as new positions
            notifyItemChanged(selected_position);
            selected_position = getAdapterPosition();
            notifyItemChanged(selected_position);
            // Do your another stuff for your onClick

            HoursRecord hoursRecord = records.get(selected_position);
            Intent intent = new Intent(context, HoursEditActivity.class);
            intent.putExtra(Globals.HOURS_RECORD_KEY, hoursRecord);
            context.startActivity(intent);

        }

        public void dehighlight(){
            clearBackGround = true;
            notifyItemChanged(selected_position);
        }
    }

}

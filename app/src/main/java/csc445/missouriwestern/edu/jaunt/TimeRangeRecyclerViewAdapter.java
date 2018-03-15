package csc445.missouriwestern.edu.jaunt;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import csc445.missouriwestern.edu.jaunt.fragments.hours.TimeRange;

/**
 * Created by byan on 3/12/2018.
 */

public class TimeRangeRecyclerViewAdapter extends RecyclerView.Adapter<TimeRangeItemViewHolder> {

    private RecyclerViewItemOnClickedListener listener;
    private Context context;
    private List<TimeRange> timeRanges;
    private int selected_position = -1;
    private boolean servingStart = true;

    public TimeRangeRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public TimeRangeRecyclerViewAdapter(Context context, List<TimeRange> timeRanges) {
        this.context = context;
        this.timeRanges = timeRanges;
    }

    public void setTimeRanges(List<TimeRange> timeRanges){
        this.timeRanges = timeRanges;
        notifyDataSetChanged();
    }

    public void setListener(RecyclerViewItemOnClickedListener i){
        listener = i;
    }

    @Override
    public TimeRangeItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_time_range, parent, false);
        TimeRangeItemViewHolder timeRangeItemViewHolder = new TimeRangeItemViewHolder(context, view);
        return timeRangeItemViewHolder;
    }

    @Override
    public void onBindViewHolder(TimeRangeItemViewHolder holder, int position) {
        TimeRange timeRange = timeRanges.get(position);
        // bind your view here
        // code moved to the holder class itself with a listener for click event
        holder.bindView(position, timeRange, listener, selected_position, servingStart);
    }

    public int getSelected_position(){
        return selected_position;
    }

    public void setSelected_position(int selected_position){
        this.selected_position = selected_position;
    }

    public void setServingStart(boolean b){
        servingStart = b;
    }

    @Override
    public int getItemCount() {
        if(timeRanges == null) {
            return 0;
        }
        return timeRanges.size();
    }

    public interface RecyclerViewItemOnClickedListener{
        void recyclerViewItemClicked(RecyclerView.ViewHolder viewHolder);

        //update the clock once called
        void timeRangeClicked(int seqNum, TimeRange timeRange, TextView textView);
    }
}

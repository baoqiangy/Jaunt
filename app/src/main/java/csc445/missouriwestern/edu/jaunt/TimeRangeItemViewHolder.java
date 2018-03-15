package csc445.missouriwestern.edu.jaunt;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import csc445.missouriwestern.edu.jaunt.fragments.hours.TimeRange;

/**
 * Created by byan on 3/12/2018.
 */

public class TimeRangeItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private TextView    seqNumTextView;
    private TextView startTimeTextView;
    private TextView   endTimeTextView;
    private View underline;

    private Context context;
    public View rootView;
    public TimeRange timeRange;
    private int seqNum;
    public TimeRangeRecyclerViewAdapter.RecyclerViewItemOnClickedListener clickListener;

    public TimeRangeItemViewHolder(Context context, View view) {
        super(view);
        this.context = context;
        rootView = view;
        seqNumTextView = view.findViewById(R.id.seq_num);
        startTimeTextView = view.findViewById(R.id.start_time);
        endTimeTextView = view.findViewById(R.id.end_time);
    }

    void bindView(int seqNum, TimeRange timeRange, TimeRangeRecyclerViewAdapter.RecyclerViewItemOnClickedListener listener, int seqNumClicked, boolean servingStart) {
        this.seqNum = seqNum;
        this.timeRange = timeRange;
        clickListener = listener;

        if(timeRange != null) {
            seqNumTextView.setText(String.valueOf(seqNum));
            startTimeTextView.setOnClickListener(createOnClickListener());
            startTimeTextView.setText(timeRange.getStartStr());
            endTimeTextView.setOnClickListener(createOnClickListener());
            endTimeTextView.setText(timeRange.getEndStr());
            if(seqNum != seqNumClicked){
                underline = rootView.findViewById(R.id.left_underline);
                underline.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                underline = rootView.findViewById(R.id.right_underline);
                underline.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
            }else{
                if(!timeRange.isStartChanged() && !timeRange.isEndChanged()){
                    if(servingStart){
                        underline = rootView.findViewById(R.id.left_underline);
                        underline.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                    }
                }
            }
//            if(timeRange.isNewRange()){
//                startTimeTextView.setBackgroundColor(context.getResources().getColor(R.color.ef_grey));
//                endTimeTextView.setBackgroundColor(context.getResources().getColor(R.color.ef_grey));
//            }
        }
        rootView.setOnClickListener(this);
    }



    private View.OnClickListener createOnClickListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.start_time){
                    underline = rootView.findViewById(R.id.left_underline);
                    underline.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                    underline = rootView.findViewById(R.id.right_underline);
                    underline.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                }else{
                    underline = rootView.findViewById(R.id.right_underline);
                    underline.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                    underline = rootView.findViewById(R.id.left_underline);
                    underline.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                }
                clickListener.timeRangeClicked(seqNum, timeRange, (TextView) v);
            }
        };
    }

    public int getSeqNum(){
        return seqNum;
    }

    public TextView getStartTimeTextView() {
        return startTimeTextView;
    }

    public TextView getEndTimeTextView() {
        return endTimeTextView;
    }

    @Override
    public void onClick(View v) {
        clickListener.recyclerViewItemClicked(this);
    }
}

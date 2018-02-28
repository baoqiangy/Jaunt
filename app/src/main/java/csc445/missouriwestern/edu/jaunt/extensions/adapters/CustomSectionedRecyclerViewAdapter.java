package csc445.missouriwestern.edu.jaunt.extensions.adapters;

import android.support.v7.widget.RecyclerView;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

/**
 * Created by byan on 2/21/2018.
 * Idea from https://stackoverflow.com/questions/43433706/best-way-to-notify-recyclerview-adapter-from-viewholder/43434674
 */

public class CustomSectionedRecyclerViewAdapter extends SectionedRecyclerViewAdapter {
    private RecyclerViewItemOnClickedListener listener;

    public void setListener(RecyclerViewItemOnClickedListener i){
        listener = i;
    }

    public interface RecyclerViewItemOnClickedListener{
        public void recyclerViewItemClicked(RecyclerView.ViewHolder viewHolder);
    }
}

package csc445.missouriwestern.edu.jaunt.fragments.preplace;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import csc445.missouriwestern.edu.jaunt.R;

/**
 * Created by byan on 2/21/2018.
 */

public class PlaceSectionHeaderViewHolder extends RecyclerView.ViewHolder {
    public TextView sectionTitleTextView;

    public PlaceSectionHeaderViewHolder(View view) {
        super(view);
        sectionTitleTextView = view.findViewById(R.id.section_title);
    }
}

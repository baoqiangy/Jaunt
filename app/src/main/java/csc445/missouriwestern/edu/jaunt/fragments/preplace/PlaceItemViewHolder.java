package csc445.missouriwestern.edu.jaunt.fragments.preplace;

import android.location.Address;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import csc445.missouriwestern.edu.jaunt.R;
import csc445.missouriwestern.edu.jaunt.extensions.adapters.CustomSectionedRecyclerViewAdapter;

/**
 * Created by byan on 2/21/2018.
 */

class PlaceItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView streetTextView;
    public TextView cityStateTextView;
    public View rootView;
    public CustomSectionedRecyclerViewAdapter.RecyclerViewItemOnClickedListener clickListener;
    public Address address;

    public PlaceItemViewHolder(View view) {
        super(view);
        rootView = view;
        streetTextView = view.findViewById(R.id.street);
        cityStateTextView = view.findViewById(R.id.city_state);
    }

    void bindView(Address address, CustomSectionedRecyclerViewAdapter.RecyclerViewItemOnClickedListener listener) {
        this.address = address;
        clickListener = listener;
        streetTextView.setText(address.getAddressLine(0));
        cityStateTextView.setText(address.getSubLocality() +", "+address.getLocality());
        rootView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        clickListener.recyclerViewItemClicked(this);
    }
}

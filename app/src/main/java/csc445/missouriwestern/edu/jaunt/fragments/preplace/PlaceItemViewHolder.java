package csc445.missouriwestern.edu.jaunt.fragments.preplace;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

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
    public String gms_id;

    private Context context;
    private Geocoder mGeocoder;

    public PlaceItemViewHolder(Context context, View view) {
        super(view);
        this.context = context;
        rootView = view;
        streetTextView = view.findViewById(R.id.street);
        cityStateTextView = view.findViewById(R.id.city_state);
        mGeocoder = new Geocoder(context, Locale.getDefault());
    }
//
//    public PlaceItemViewHolder(View view) {
//        super(view);
//        rootView = view;
//        streetTextView = view.findViewById(R.id.street);
//        cityStateTextView = view.findViewById(R.id.city_state);
//    }
//
    void bindView(String gms_id, Address address, CustomSectionedRecyclerViewAdapter.RecyclerViewItemOnClickedListener listener) {
        this.gms_id = gms_id;
        this.address = address;
        clickListener = listener;

        //LatLng latLng = address.getLatLng();
        //Address androidAddress = GeocoderUtils.getAddressByCoordinates(mGeocoder, latLng);
        if(address != null) {
            streetTextView.setText(address.getAddressLine(0).split(",")[0]);
            cityStateTextView.setText(address.getLocality() +", "+address.getAdminArea());
        }
        rootView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        clickListener.recyclerViewItemClicked(this);
    }
}

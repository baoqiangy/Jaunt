package csc445.missouriwestern.edu.jaunt;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import csc445.missouriwestern.edu.jaunt.model.Delivery;
import csc445.missouriwestern.edu.jaunt.utils.date.TimeWrapper;
import csc445.missouriwestern.edu.jaunt.utils.places.AddressUtils;

/**
 * Created by byan on 3/15/2018.
 */

public class DeliveryItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private Context context;
    public View rootView;
    public Delivery delivery;
    public DeliveryRecyclerViewAdapter.RecyclerViewItemOnClickedListener clickListener;

    private TextView deliveryAddressTextView;
    private TextView distanceTextView;
    private TextView orderAmountTextView;
    private TextView tipsTextView;
    private TextView deliveryFeeTextView;
    private TextView pickupTimeTextView;
    private TextView deliverTimeTextView;


    public DeliveryItemViewHolder(Context context, View view) {
        super(view);
        this.context = context;
        rootView = view;
        deliveryAddressTextView = view.findViewById(R.id.delivery_address);
        distanceTextView = view.findViewById(R.id.distance);
        orderAmountTextView = view.findViewById(R.id.order_amount);
        tipsTextView = view.findViewById(R.id.tips);
        deliveryFeeTextView = view.findViewById(R.id.delivery_fee);
        pickupTimeTextView = view.findViewById(R.id.pickup_time);
        deliverTimeTextView = view.findViewById(R.id.deliver_time);
    }

    public void bindView(Delivery delivery, DeliveryRecyclerViewAdapter.RecyclerViewItemOnClickedListener listener) {
        this.delivery = delivery;
        clickListener = listener;

        if(delivery != null) {
            deliveryAddressTextView.setText(AddressUtils.addressToString(delivery.getDeliveryaddress()));
            distanceTextView.setText(String.valueOf("23.5") + " miles");
            orderAmountTextView.setText("$"+String.format("%.2f", delivery.getTotalAmount()));
            pickupTimeTextView.setText("Pickup: "+TimeWrapper.jodaTimeToFullDisplayStr(delivery.getScheduledPickupTime()));
            deliverTimeTextView.setText("Deliver: "+TimeWrapper.jodaTimeToFullDisplayStr(delivery.getScheduledDeliverTime()));
            deliveryFeeTextView.setText("$"+String.format("%.2f", delivery.getDeliveryFee()));
            if(Math.round(delivery.getTips()) < 0.001) {
                tipsTextView.setText("TBD");
            }else{
                tipsTextView.setText("$"+String.format("%.2f", delivery.getTips()));
            }
        }
        rootView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        clickListener.recyclerViewItemClicked(this);
    }
}

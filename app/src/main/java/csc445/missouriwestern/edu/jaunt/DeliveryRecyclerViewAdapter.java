package csc445.missouriwestern.edu.jaunt;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import csc445.missouriwestern.edu.jaunt.model.Delivery;

/**
 * Created by byan on 3/15/2018.
 */

public class DeliveryRecyclerViewAdapter extends RecyclerView.Adapter<DeliveryItemViewHolder> {

    private Context context;
    private List<Delivery> available_deliveries;
    private DeliveryRecyclerViewAdapter.RecyclerViewItemOnClickedListener listener;
    private String TAG = "TAG_DeliveryRecyclerViewAdapter";

    public DeliveryRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public DeliveryRecyclerViewAdapter(Context context, List<Delivery> available_deliveries) {
        this.context = context;
        this.available_deliveries = available_deliveries;
    }

    public void setListener(DeliveryRecyclerViewAdapter.RecyclerViewItemOnClickedListener i){
        listener = i;
    }

    @Override
    public DeliveryItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_available_delivery, parent, false);
        DeliveryItemViewHolder deliveryItemViewHolder = new DeliveryItemViewHolder(context, view);
        return deliveryItemViewHolder;
    }

    @Override
    public void onBindViewHolder(DeliveryItemViewHolder holder, int position) {
        holder.bindView(available_deliveries.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return available_deliveries.size();
    }

    public interface RecyclerViewItemOnClickedListener{
        void recyclerViewItemClicked(RecyclerView.ViewHolder viewHolder);
    }
}

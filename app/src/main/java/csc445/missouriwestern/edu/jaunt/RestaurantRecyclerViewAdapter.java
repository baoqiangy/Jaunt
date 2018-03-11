package csc445.missouriwestern.edu.jaunt;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import csc445.missouriwestern.edu.jaunt.model.Restaurant;

/**
 * Created by byan on 3/8/2018.
 */

public class RestaurantRecyclerViewAdapter extends RecyclerView.Adapter<RestaurantItemViewHolder> {

    private RecyclerViewItemOnClickedListener listener;

    private Context context;
    private List<Restaurant> restaurants;
    private int selected_position = 0;

    public RestaurantRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public RestaurantRecyclerViewAdapter(Context context, List<Restaurant> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
    }

    public void setListener(RecyclerViewItemOnClickedListener i){
        listener = i;
    }

    @Override
    public RestaurantItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_restaurant, parent, false);
        RestaurantItemViewHolder restaurantItemViewHolder = new RestaurantItemViewHolder(context, view);
        return restaurantItemViewHolder;
    }

    @Override
    public void onBindViewHolder(RestaurantItemViewHolder holder, int position) {
        Restaurant restaurant = restaurants.get(position);
        // bind your view here
        // code moved to the holder class itself with a listener for click event
        holder.bindView(restaurant, listener);
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    @Override
    public void onViewAttachedToWindow(RestaurantItemViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.startFlashing();
    }

    @Override
    public void onViewDetachedFromWindow(RestaurantItemViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.stopFlashing();
    }

    public interface RecyclerViewItemOnClickedListener{
        void recyclerViewItemClicked(RecyclerView.ViewHolder viewHolder);
    }
}

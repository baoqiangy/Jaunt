package csc445.missouriwestern.edu.jaunt;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import csc445.missouriwestern.edu.jaunt.model.Restaurant;

/**
 * Created by byan on 3/8/2018.
 */

public class RestaurantItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private ImageView restaurantImageView;
    private TextView restaurantTitleTextView;
    private TextView restaurantStreeTextView;
    private TextView restaurantCityStateTextView;

    private Context context;
    public View rootView;
    public Restaurant restaurant;
    public RestaurantRecyclerViewAdapter.RecyclerViewItemOnClickedListener clickListener;

    public RestaurantItemViewHolder(Context context, View view) {
        super(view);
        this.context = context;
        rootView = view;
        restaurantImageView = view.findViewById(R.id.restaurant_image);
        restaurantTitleTextView = view.findViewById(R.id.restaurant_name);
        restaurantStreeTextView = view.findViewById(R.id.restaurant_street);
        restaurantCityStateTextView = view.findViewById(R.id.restaurant_city_state);
    }

    void bindView(Restaurant restaurant, RestaurantRecyclerViewAdapter.RecyclerViewItemOnClickedListener listener) {
        this.restaurant = restaurant;
        clickListener = listener;

        if(restaurant != null) {
            String imageUrl = Globals.SERVER_DOMAIN+"/"+"menuhunt/uploads/restauranticons/"+restaurant.getRid()+"/"+restaurant.getLogo();
            RequestOptions cropOptions = new RequestOptions().centerCrop();
            if(restaurant.getLogo().length()>0){
                Glide.with(context).load(imageUrl).apply(cropOptions).into(restaurantImageView);

            }else{
                restaurantImageView.setImageResource(R.drawable.restaurant_placeholder);
            }
            restaurantImageView.setClipToOutline(true);
            restaurantTitleTextView.setText(restaurant.getName());
            restaurantStreeTextView.setText(restaurant.getAddress().getAddressLine(0));
            restaurantCityStateTextView.setText(restaurant.getAddress().getLocality() + ", " + restaurant.getAddress().getAdminArea());
        }
        rootView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        clickListener.recyclerViewItemClicked(this);
    }
}

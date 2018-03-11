package csc445.missouriwestern.edu.jaunt;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
    private ImageView flashImageView;

    private Context context;
    public View rootView;
    public Restaurant restaurant;
    public RestaurantRecyclerViewAdapter.RecyclerViewItemOnClickedListener clickListener;

    private Animation alphaAnimationIn;
    private Animation alphaAnimationOut;

    public RestaurantItemViewHolder(Context context, View view) {
        super(view);
        this.context = context;
        rootView = view;
        restaurantImageView = view.findViewById(R.id.restaurant_image);
        restaurantTitleTextView = view.findViewById(R.id.restaurant_name);
        restaurantStreeTextView = view.findViewById(R.id.restaurant_street);
        restaurantCityStateTextView = view.findViewById(R.id.restaurant_city_state);
        flashImageView = view.findViewById(R.id.flash_image);
        //setUpAnimations();
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
            if(Math.random() < 0.5){
                flashImageView.setImageResource(R.drawable.ic_add_circle_24dp);
            }else{
                flashImageView.setImageResource(R.drawable.ic_check_circle_36dp);
            }
        }
        rootView.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        clickListener.recyclerViewItemClicked(this);
    }

    public void startFlashing() {
        //flashImageView.startAnimation(alphaAnimationIn);
    }

    public void stopFlashing() {
        //flashImageView.clearAnimation();
    }

    private void setUpAnimations() {
        alphaAnimationIn = AnimationUtils.loadAnimation(context, R.anim.anim_alpha_in);
        alphaAnimationIn.setDuration(1000);
        alphaAnimationOut = AnimationUtils.loadAnimation(context, R.anim.anim_alpha_out);
        alphaAnimationOut.setDuration(1000);

        alphaAnimationIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                flashImageView.startAnimation(alphaAnimationOut);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        alphaAnimationOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                flashImageView.startAnimation(alphaAnimationIn);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}

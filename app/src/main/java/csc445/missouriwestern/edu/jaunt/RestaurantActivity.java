package csc445.missouriwestern.edu.jaunt;

import android.graphics.drawable.Drawable;
import android.location.Address;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import csc445.missouriwestern.edu.jaunt.model.Restaurant;

public class RestaurantActivity extends AppCompatActivity {
    private Restaurant restaurant;
    private ImageView appBarImageView;
    private LinearLayout blurView;
    private TextView nameTextView;
    private TextView addressTextView;
    private RecyclerView weatherRecyclerView;
    private RecyclerView availableDeliveryRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        wireUpWidgets();
        restaurant = (Restaurant) getIntent().getParcelableExtra("restaurant");
        String imageUrl = Globals.SERVER_DOMAIN+"/"+"menuhunt/uploads/restauranticons/"+restaurant.getRid()+"/"+restaurant.getLogo();
        if(restaurant.getLogo().length()>0){
            Glide.with(this).load(imageUrl).
                    listener(createGlideListener_RestaurantLogo()).
                    into(appBarImageView);
        }else{
            appBarImageView.setImageResource(R.drawable.restaurant_placeholder);
            generateBlurEffectForBottomTextViews();
        }
    }

    private RequestListener<Drawable> createGlideListener_RestaurantLogo() {
        return new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                appBarImageView.setImageResource(R.drawable.restaurant_placeholder);
                Toast.makeText(RestaurantActivity.this, "Restaurant logo download failed.", Toast.LENGTH_SHORT).show();
                generateBlurEffectForBottomTextViews();
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                generateBlurEffectForBottomTextViews();
                return false;
            }
        };
    }

    private void generateBlurEffectForBottomTextViews(){
        // View
        //BlurKit.getInstance().blur(blurView, 8);

        // Bitmap
        //BlurKit.getInstance().blur(Bitmap src, int radius);
        nameTextView.setText(restaurant.getName());
        Address address = restaurant.getAddress();
        String street = address.getAddressLine(0);
        String city = address.getLocality();
        String state = address.getAdminArea();
        addressTextView.setText(street + "\n" + city + ", " + state);
    }

    private void wireUpWidgets() {
        appBarImageView = findViewById(R.id.app_bar_image);
        blurView = findViewById(R.id.restaurant_info_blurview);
        nameTextView = blurView.findViewById(R.id.restaurant_name);
        addressTextView = blurView.findViewById(R.id.appbar_address);
        weatherRecyclerView = findViewById(R.id.weather_recyclerview);
        availableDeliveryRecyclerView = findViewById(R.id.available_delivery_recyclerview);
    }


}

package csc445.missouriwestern.edu.jaunt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import csc445.missouriwestern.edu.jaunt.model.Restaurant;

public class RestaurantActivity extends AppCompatActivity {
    private Restaurant restaurant;
    private ImageView appBarImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        wireUpWidgets();
        restaurant = (Restaurant) getIntent().getParcelableExtra("restaurant");
        String imageUrl = Globals.SERVER_DOMAIN+"/"+"menuhunt/uploads/restauranticons/"+restaurant.getRid()+"/"+restaurant.getLogo();
        if(restaurant.getLogo().length()>0){
            Glide.with(this).load(imageUrl).into(appBarImageView);
        }else{
            appBarImageView.setImageResource(R.drawable.restaurant_placeholder);
        }
    }

    private void wireUpWidgets() {
        appBarImageView = findViewById(R.id.app_bar_image);
    }
}

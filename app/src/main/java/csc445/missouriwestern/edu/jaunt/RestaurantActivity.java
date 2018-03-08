package csc445.missouriwestern.edu.jaunt;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import csc445.missouriwestern.edu.jaunt.model.Restaurant;

public class RestaurantActivity extends AppCompatActivity {
    private Restaurant restaurant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        restaurant = (Restaurant) getIntent().getParcelableExtra("restaurant");
        TextView nameTV = findViewById(R.id.restaurant_name);
        if(nameTV != null){
            nameTV.setText(restaurant.getName());
        }
    }
}

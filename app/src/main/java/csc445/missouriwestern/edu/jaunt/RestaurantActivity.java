package csc445.missouriwestern.edu.jaunt;

import android.graphics.drawable.Drawable;
import android.location.Address;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import csc445.missouriwestern.edu.jaunt.model.Restaurant;
import csc445.missouriwestern.edu.jaunt.utils.places.AddressUtils;

public class RestaurantActivity extends AppCompatActivity {
    private Restaurant restaurant;
    private Address address;
    private int cityId;
    private ImageView appBarImageView;
    private LinearLayout blurView;
    private TextView nameTextView;
    private TextView addressTextView;
    private RecyclerView weatherRecyclerView;
    private LinearLayoutManager horizontalLayoutManager;
    private JSONArray broadcasts;
    private RecyclerView availableDeliveryRecyclerView;
    private LinearLayoutManager verticalLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        wireUpWidgets();
        restaurant = (Restaurant) getIntent().getParcelableExtra("restaurant");
        address = restaurant.getAddress();
        String imageUrl = Globals.SERVER_DOMAIN+"/"+"menuhunt/uploads/restauranticons/"+restaurant.getRid()+"/"+restaurant.getLogo();
        if(restaurant.getLogo().length()>0){
            Glide.with(this).load(imageUrl).
                    listener(createGlideListener_RestaurantLogo()).
                    into(appBarImageView);
        }else{
            appBarImageView.setImageResource(R.drawable.restaurant_placeholder);
            generateBlurEffectForBottomTextViews();
        }
        loadWeatherData();
    }

    private void loadWeatherData() {

        cityId = AddressUtils.getCityId(address);
        Log.d("CITY_ID", String.valueOf(cityId));

//        broadcasts = PersistenceWrapper.loadWeatherData();
//        if(broadcasts == null){
//            fetchWeather();
//            return;
//        }

        //refresh the weather display
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
        horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        verticalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }

    private void fetchWeather(){
        try{
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String script = "list_restaurant.php";
            JSONObject params = new JSONObject();
            params.put("units", "metric");
            params.put("lat", address.getLatitude());
            params.put("lon", address.getLongitude());
            params.put("appid", this.getString(R.string.openweatherapi_key));

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    Globals.OPEN_WEATHER_MAP_SERVER,
                    params,
                    createListener_fetchWeather(),
                    createErrorListener_fetchWeather());

            //progressTxtView.setText("Processing ...");
            requestQueue.add(jsonObjectRequest);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private Response.Listener<JSONObject> createListener_fetchWeather(){
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //progressTxtView.setText("Processing ... Done !");
//                try{
//                    int success = response.getInt("cod");
//                    if(success == 200) {
//                        int count = response.getInt("cnt");
//                        JSONArray broadcasts = response.getJSONArray("list");
//                        for(int i=0; i<broadcasts.length(); i++){
//
//                        }
//                        JSONObject jsonWeather   = response.getJSONObject("weather");
//                        JSONObject jsonClouds    = response.getJSONObject("clouds");
//                        JSONObject jsonWind      = response.getJSONObject("wind");
//                        JSONObject jsonMain      = response.getJSONObject("main");
//
//                        restaurants = new ArrayList<Restaurant>();
//                        JSONObject jsonObject = null;
//                        for (int i=0; i<jsonObjects.length(); i++) {
//                            jsonObject = (JSONObject)jsonObjects.get(i);
//                            restaurants.add(new Restaurant(jsonObject));
//                        }
//
//                        updateRecyclerView();
//
//                        Paper.book(Globals.GUEST_BOOK).write("restaurants", restaurants);
//                    }else{
//                        Log.d(TAG, response.getString("error_message"));
//                        Toast.makeText(RestaurantsActivity.this, response.getString("error_message"), Toast.LENGTH_SHORT).show();
//                    }
//                }
//                catch (Exception e){
//                    e.printStackTrace();
//                    Toast.makeText(RestaurantsActivity.this, "Exception occurred." + e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
            }
        };
    }

    private Response.ErrorListener createErrorListener_fetchWeather(){
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressTxtView.setText("Processing ... Error!");
//                Log.d(TAG, error.getMessage());
//                Toast.makeText(RestaurantsActivity.this, "Error response from server - " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }
}

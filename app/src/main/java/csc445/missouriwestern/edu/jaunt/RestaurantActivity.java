package csc445.missouriwestern.edu.jaunt;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import csc445.missouriwestern.edu.jaunt.model.Delivery;
import csc445.missouriwestern.edu.jaunt.model.Restaurant;
import csc445.missouriwestern.edu.jaunt.utils.recyclerview.DividerItemDecorator;

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
    private WeatherRecyclerViewAdapter weatherRecyclerViewAdapter;
    private JSONArray forcasts;
    private WeatherRecyclerViewAdapter.RecyclerViewItemOnClickedListener weatherItemClickListener;

    private RecyclerView availableDeliveryRecyclerView;
    private LinearLayoutManager verticalLayoutManager;
    private DeliveryRecyclerViewAdapter deliveryRecyclerViewAdapter;
    private DeliveryRecyclerViewAdapter.RecyclerViewItemOnClickedListener deliveryItemClickListener;
    private List<Delivery> availableDeliveries;
    private Context context;
    private String TAG = "TAG_RestaurantActivity";
    private boolean favorite;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        context = this;
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
        loadAvailableDeliveries();
    }

    private void loadAvailableDeliveries() {
        fetchAvailableDeliveries();
    }

    private void loadWeatherData() {
        int city_id = restaurant.getCity_id();
        if(city_id == 0) return;
        fetchWeather();

        //cityId = AddressUtils.getCityId(address);
        //Log.d("CITY_ID", String.valueOf(cityId));

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
        fab = findViewById(R.id.favorite_fab);
        blurView = findViewById(R.id.restaurant_info_blurview);
        nameTextView = blurView.findViewById(R.id.restaurant_name);
        addressTextView = blurView.findViewById(R.id.appbar_address);
        weatherRecyclerView = findViewById(R.id.weather_recyclerview);
        availableDeliveryRecyclerView = findViewById(R.id.available_delivery_recyclerview);
        horizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        verticalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }

    private void fetchWeather(){
        int city_id = restaurant.getCity_id();
        if(city_id == 0) return;

        String url = String.format(Globals.OPEN_WEATHER_MAP_API, city_id, this.getString(R.string.openweatherapi_key));
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                createListener_fetchWeather(),
                createErrorListener_fetchWeather());

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private Response.Listener<String> createListener_fetchWeather(){
        return new Response.Listener<String >() {
            @Override
            public void onResponse(String response) {
                //progressTxtView.setText("Processing ... Done !");
                try{
                    JSONObject responseJsonObject = new JSONObject(response);
                    int cod = responseJsonObject.getInt("cod");
                    if(cod == 200) {
                        int count = responseJsonObject.getInt("cnt");
                        forcasts = responseJsonObject.getJSONArray("list");
                        updateWeatherRecyclerView();
                    }else{
                        Toast.makeText(RestaurantActivity.this, "Error when fetching weather info.", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(RestaurantActivity.this, "Exception occurred." + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private Response.ErrorListener createErrorListener_fetchWeather(){
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressTxtView.setText("Processing ... Error!");
//                Log.d(TAG, error.getMessage());
                Toast.makeText(RestaurantActivity.this, "Error response from server - " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void updateWeatherRecyclerView() {
        weatherRecyclerViewAdapter = new WeatherRecyclerViewAdapter(context, forcasts);
        createListenerIfNotExist();
        weatherRecyclerViewAdapter.setListener(weatherItemClickListener);
        weatherRecyclerView.setAdapter(weatherRecyclerViewAdapter);
        weatherRecyclerView.setLayoutManager(horizontalLayoutManager);

    }

    private void createListenerIfNotExist(){
        weatherItemClickListener = new WeatherRecyclerViewAdapter.RecyclerViewItemOnClickedListener(){
            @Override
            public void recyclerViewItemClicked(RecyclerView.ViewHolder viewHolder) {
                JSONObject selectedCast = ((WeatherItemViewHolder)viewHolder).weatherForcast;
                try{
                    Toast.makeText(RestaurantActivity.this, selectedCast.getString("dt_txt"), Toast.LENGTH_SHORT).show();
                }catch (JSONException e){
                    Toast.makeText(RestaurantActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };

        deliveryItemClickListener = new DeliveryRecyclerViewAdapter.RecyclerViewItemOnClickedListener() {
            @Override
            public void recyclerViewItemClicked(RecyclerView.ViewHolder viewHolder) {

            }
        };
    }

    private void fetchAvailableDeliveries() {
        try{
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String script = "pending_delivery_orders.php";
            JSONObject params = new JSONObject();
            params.put("rid", "14");

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    Globals.SERVER_URL + "/" + script,
                    params,
                    createListener_fetchAvailableDeliveries(),
                    createErrorListener_fetchAvailableDeliveries());

            //progressTxtView.setText("Processing ...");
            requestQueue.add(jsonObjectRequest);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private Response.Listener<JSONObject> createListener_fetchAvailableDeliveries(){
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //progressTxtView.setText("Processing ... Done !");
                try{
                    int success = response.getInt("success");
                    if(success == 1) {

                        JSONArray jsonObjects = response.getJSONArray("available_deliveries");
                        availableDeliveries = new ArrayList<Delivery>();
                        JSONObject jsonObject = null;
                        for (int i=0; i<jsonObjects.length(); i++) {
                            jsonObject = (JSONObject)jsonObjects.get(i);
                            availableDeliveries.add(new Delivery(jsonObject));
                            availableDeliveries.add(new Delivery(jsonObject));
                            availableDeliveries.add(new Delivery(jsonObject));
                            availableDeliveries.add(new Delivery(jsonObject));
                            availableDeliveries.add(new Delivery(jsonObject));
                            availableDeliveries.add(new Delivery(jsonObject));
                            availableDeliveries.add(new Delivery(jsonObject));
                        }

                        updateAvailableDeliveryRecyclerView();

                    }else{
                        Log.d(TAG, response.getString("error_message"));
                        Toast.makeText(RestaurantActivity.this, response.getString("error_message"), Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(RestaurantActivity.this, "Exception occurred." + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private Response.ErrorListener createErrorListener_fetchAvailableDeliveries(){
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressTxtView.setText("Processing ... Error!");
                Log.d(TAG, error.getMessage());
                Toast.makeText(RestaurantActivity.this, "Error response from server - " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void updateAvailableDeliveryRecyclerView() {
        deliveryRecyclerViewAdapter = new DeliveryRecyclerViewAdapter(context, availableDeliveries);
        createListenerIfNotExist();
        deliveryRecyclerViewAdapter.setListener(deliveryItemClickListener);
        availableDeliveryRecyclerView.setAdapter(deliveryRecyclerViewAdapter);

        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(availableDeliveryRecyclerView.getContext(),
        //        verticalLayoutManager.getOrientation());
        //availableDeliveryRecyclerView.addItemDecoration(dividerItemDecoration);

        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(context, R.drawable.divider));
        availableDeliveryRecyclerView.addItemDecoration(dividerItemDecoration);

        availableDeliveryRecyclerView.setLayoutManager(verticalLayoutManager);
    }

    public void toggleFavorite(View view) {
        favorite = !favorite;
        if(favorite){
            fab.setImageResource(R.drawable.ic_favorite_white_solid_24dp);
        }else{
            fab.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        }
        Toast.makeText(this, "Toggling favorite for this restaurant", Toast.LENGTH_SHORT).show();
    }
}

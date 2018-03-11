package csc445.missouriwestern.edu.jaunt;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import csc445.missouriwestern.edu.jaunt.model.Restaurant;
import io.paperdb.Paper;

public class RestaurantsActivity extends AppCompatActivity {

    private String TAG = "TAG_RestaurantsActivity";
    private Context context;
    private RecyclerView restaurantRecyclerView;
    private RestaurantRecyclerViewAdapter restaurantRecyclerViewAdapter;
    private List<Restaurant> restaurants;
    RestaurantRecyclerViewAdapter.RecyclerViewItemOnClickedListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        context = this;
        restaurantRecyclerView = findViewById(R.id.restaurant_recyclerview);
        Paper.book(Globals.GUEST_BOOK).read("restaurants", null);
        if(restaurants == null) {
            fetchLocalRestaurants();
        }else{
            restaurantRecyclerViewAdapter = new RestaurantRecyclerViewAdapter(context, restaurants);
            createListenerIfNotExist();
            restaurantRecyclerViewAdapter.setListener(listener);
            restaurantRecyclerView.setAdapter(restaurantRecyclerViewAdapter);
        }
        customizeActionBar();
    }

    @SuppressLint("ResourceAsColor")
    private void customizeActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        //actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        //actionBar.setBackgroundDrawable(new ColorDrawable(R.color.colorFocus));
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        TextView tv = new TextView(context);
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/AvenirNextCondensed-Regular.ttf");
        tv.setTypeface(face);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f);
        tv.setText("Restaurants");

        actionBar.setCustomView(tv);
        //actionBar.setTitle("Restaurants");
        //actionBar.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Log.d(TAG, "action bar clicked");
        }
        return super.onOptionsItemSelected(item);
    }

    private void createListenerIfNotExist(){
        listener = new RestaurantRecyclerViewAdapter.RecyclerViewItemOnClickedListener() {
            @Override
            public void recyclerViewItemClicked(RecyclerView.ViewHolder viewHolder) {
                Restaurant selectedRestaurant = ((RestaurantItemViewHolder)viewHolder).restaurant;
                Intent intent = new Intent(RestaurantsActivity.this, RestaurantActivity.class);
                intent.putExtra("restaurant", (Parcelable) selectedRestaurant);
                startActivity(intent);
            }
        };
    }

    private void fetchLocalRestaurants(){
        try{
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String script = "list_restaurant.php";
            JSONObject params = new JSONObject();
            params.put("purpose", "driver");

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    Globals.SERVER_URL + "/" + script,
                    params,
                    createListener_fetchRestaurants(),
                    createErrorListener_fetchRestaurants());

            //progressTxtView.setText("Processing ...");
            requestQueue.add(jsonObjectRequest);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private Response.Listener<JSONObject> createListener_fetchRestaurants(){
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //progressTxtView.setText("Processing ... Done !");
                try{
                    int success = response.getInt("success");
                    if(success == 1) {

                        JSONArray jsonObjects = response.getJSONArray("restaurants");

                        restaurants = new ArrayList<Restaurant>();
                        JSONObject jsonObject = null;
                        for (int i=0; i<jsonObjects.length(); i++) {
                            jsonObject = (JSONObject)jsonObjects.get(i);
                            restaurants.add(new Restaurant(jsonObject));
                        }

                        restaurantRecyclerViewAdapter = new RestaurantRecyclerViewAdapter(context, restaurants);
                        createListenerIfNotExist();
                        restaurantRecyclerViewAdapter.setListener(listener);
                        restaurantRecyclerView.setAdapter(restaurantRecyclerViewAdapter);
                        restaurantRecyclerView.setLayoutManager(new LinearLayoutManager(context));

                        Paper.book(Globals.GUEST_BOOK).write("restaurants", restaurants);
                    }else{
                        Log.d(TAG, response.getString("error_message"));
                        Toast.makeText(RestaurantsActivity.this, response.getString("error_message"), Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(RestaurantsActivity.this, "Exception occurred." + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private Response.ErrorListener createErrorListener_fetchRestaurants(){
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressTxtView.setText("Processing ... Error!");
                Log.d(TAG, error.getMessage());
                Toast.makeText(RestaurantsActivity.this, "Error response from server - " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }
}

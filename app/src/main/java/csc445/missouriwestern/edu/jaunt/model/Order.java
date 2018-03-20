package csc445.missouriwestern.edu.jaunt.model;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import csc445.missouriwestern.edu.jaunt.Jaunt;

/**
 * Created by byan on 3/15/2018.
 */

public class Order {
    private Customer customer;
    private List<FoodItemOrder> foodItemOrders;

    public Order(Customer customer, List<FoodItemOrder> foodItemOrders) {
        this.customer = customer;
        this.foodItemOrders = foodItemOrders;
    }

    public Order(JSONObject jsonObject){
        try{
            this.customer = new Customer(jsonObject);
            this.foodItemOrders = populateFoodItemOrders(jsonObject.getJSONArray("foodorders"));
        }
        catch (JSONException e){
            Toast.makeText(Jaunt.getAppContext(), "Constructing Order - "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private List<FoodItemOrder> populateFoodItemOrders(JSONArray food_item_orders) {
        List<FoodItemOrder> foodItemOrders = new ArrayList<>();
        if(food_item_orders == null) return null;
        try {
            for(int i=0; i<food_item_orders.length(); i++){
                foodItemOrders.add(new FoodItemOrder(food_item_orders.getJSONObject(i)));
            }
        }catch (JSONException e){
            Toast.makeText(Jaunt.getAppContext(), "populateFoodItemOrders - "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return foodItemOrders;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<FoodItemOrder> getFoodItemOrders() {
        return foodItemOrders;
    }

    public void setFoodItemOrders(List<FoodItemOrder> foodItemOrders) {
        this.foodItemOrders = foodItemOrders;
    }
}

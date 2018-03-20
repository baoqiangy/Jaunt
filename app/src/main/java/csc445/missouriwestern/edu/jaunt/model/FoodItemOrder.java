package csc445.missouriwestern.edu.jaunt.model;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import csc445.missouriwestern.edu.jaunt.Jaunt;

/**
 * Created by byan on 3/15/2018.
 */

public class FoodItemOrder {
    private FoodItem foodItem;
    private String options;
    private double paidPrice;
    private int qty;

    public FoodItemOrder(FoodItem foodItem, String options, double paidPrice, int qty) {
        this.foodItem = foodItem;
        this.options = options;
        this.paidPrice = paidPrice;
        this.qty = qty;
    }

    public FoodItemOrder(JSONObject jsonObject){
        try{
            this.foodItem = new FoodItem(jsonObject);
            this.options = generateOptionsString(jsonObject.getJSONArray("options"));
            this.paidPrice = jsonObject.getDouble("originalprice") - jsonObject.getDouble("discount");
            this.qty = jsonObject.getInt("qty");
        }
        catch (JSONException e){
            Toast.makeText(Jaunt.getAppContext(), "Constructing FoodItemOrder - "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public FoodItem getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(FoodItem foodItem) {
        this.foodItem = foodItem;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public double getPaidPrice() {
        return paidPrice;
    }

    public void setPaidPrice(double paidPrice) {
        this.paidPrice = paidPrice;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String generateOptionsString(JSONArray jsonArray){
        String optionString = "";
        try {
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject option = jsonArray.getJSONObject(i);
                JSONArray subOptions = option.getJSONArray("options");
                for(int j=0; j<subOptions.length(); j++){
                    JSONObject suboption = subOptions.getJSONObject(j);
                    optionString += suboption.getString("description") +" x "+ suboption.getInt("qty");
                }
            }
        }catch (JSONException e){
            Toast.makeText(Jaunt.getAppContext(), "Constructing options string - "+e.getMessage(), Toast.LENGTH_SHORT).show();
            return "";
        }
        return optionString;
    }
}

package csc445.missouriwestern.edu.jaunt.model;

import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import csc445.missouriwestern.edu.jaunt.Jaunt;

/**
 * Created by byan on 3/15/2018.
 */

public class FoodItem {
    private int fid;
    private String name;
    private double price;

    public FoodItem(int fid, String name, double price) {
        this.fid = fid;
        this.name = name;
        this.price = price;
    }

    public FoodItem(JSONObject jsonObject){
        try{
            this.fid = jsonObject.getInt("fid");
            this.name = jsonObject.getString("fname");
            this.price = jsonObject.getDouble("originalprice");
        }
        catch (JSONException e){
            Toast.makeText(Jaunt.getAppContext(), "Constructing FoodItem - "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

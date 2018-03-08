package csc445.missouriwestern.edu.jaunt.model;

import android.location.Address;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by byan on 3/8/2018.
 */

public class Restaurant {
    private int rid;
    private String name;
    private String telephone;
    private String logo;
    private String gms_id;
    private LatLng latLng;
    private Address address;
    private RestaurantMenu menu;

    private String TAG = "TAG_Restaurant";

    public Restaurant(JSONObject jsonObject) {
        try {
            JSONObject basicInfoJson = jsonObject.getJSONObject("rinfo");
            this.rid        = basicInfoJson.getInt("rid");
            this.name       = basicInfoJson.getString("name");
            this.telephone  = basicInfoJson.getString("tel");
            this.logo       = basicInfoJson.getString("icon");
            this.latLng     = new LatLng(basicInfoJson.getDouble("latitude"), basicInfoJson.getDouble("longitude"));
            this.gms_id     = null;
            this.address = new Address(Locale.getDefault());
            this.address.setAddressLine(0, basicInfoJson.getString("street"));
            this.address.setLocality(basicInfoJson.getString("city"));
            this.address.setAdminArea(basicInfoJson.getString("state"));
            this.address.setPostalCode(basicInfoJson.getString("zipcode"));
        }catch (JSONException e){
            Log.d(TAG, e.getMessage());
        }
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getGms_id() {
        return gms_id;
    }

    public void setGms_id(String gms_id) {
        this.gms_id = gms_id;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public RestaurantMenu getMenu() {
        return menu;
    }

    public void setMenu(RestaurantMenu menu) {
        this.menu = menu;
    }
}

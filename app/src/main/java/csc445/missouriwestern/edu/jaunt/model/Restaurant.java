package csc445.missouriwestern.edu.jaunt.model;

import android.location.Address;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by byan on 3/8/2018.
 */

public class Restaurant implements Parcelable{
    private int rid;
    private String name;
    private String telephone;
    private String logo;
    private String gms_id;
    private LatLng latLng;
    private Address address;
    private int city_id; //for weather info at openweathermap.org
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
            this.city_id = basicInfoJson.getInt("city_id");
        }catch (JSONException e){
            Log.d(TAG, e.getMessage());
        }
    }

    protected Restaurant(Parcel in) {
        rid = (Integer) in.readSerializable();
        name = (String) in.readSerializable();
        telephone = (String) in.readSerializable();
        logo = (String) in.readSerializable();
        gms_id = (String) in.readSerializable();
        latLng = (LatLng) in.readParcelable(LatLng.class.getClassLoader());
        address = (Address) in.readParcelable(Address.class.getClassLoader());
        city_id = (Integer) in.readSerializable();
        menu = (RestaurantMenu) in.readParcelable(RestaurantMenu.class.getClassLoader());
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

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

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public RestaurantMenu getMenu() {
        return menu;
    }

    public void setMenu(RestaurantMenu menu) {
        this.menu = menu;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(new Integer(this.rid));
        dest.writeSerializable(this.name);
        dest.writeSerializable(this.telephone);
        dest.writeSerializable(this.logo);
        dest.writeSerializable(this.gms_id);
        dest.writeParcelable(this.latLng, flags);
        dest.writeParcelable(this.address, flags);
        dest.writeSerializable(new Integer(this.city_id));
        dest.writeParcelable(this.menu, flags);
    }
}

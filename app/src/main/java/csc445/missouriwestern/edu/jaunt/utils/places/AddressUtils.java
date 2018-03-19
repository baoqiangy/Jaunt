package csc445.missouriwestern.edu.jaunt.utils.places;

import android.location.Address;
import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import csc445.missouriwestern.edu.jaunt.Globals;
import csc445.missouriwestern.edu.jaunt.Jaunt;
import csc445.missouriwestern.edu.jaunt.utils.Json.JsonLoader;

/**
 * Created by byan on 3/3/2018.
 */

public class AddressUtils {
    public static Address jsonToAddress(JSONObject jsonObject){
        Address address = null;
        try{
            address = new Address(Locale.getDefault());
            address.setAddressLine(0, jsonObject.getString("street1"));
            address.setLocality(jsonObject.getString("city"));
            address.setAdminArea(jsonObject.getString("state"));
            address.setPostalCode(jsonObject.getString("zipcode"));
        }catch (Exception e){
            Toast.makeText(Jaunt.getAppContext(), "Could not create address from json.", Toast.LENGTH_SHORT).show();
            address = null;
        }
        return address;
    }

    public static JSONObject addressToJson(Address address, String gms_id){
        JSONObject jsonObject = new JSONObject();
        try{
            if(gms_id != null){
                jsonObject.put("gms_id", gms_id);
            }

            jsonObject.put("street1", address.getAddressLine(0));
            jsonObject.put("city", address.getLocality());
            jsonObject.put("state", toStateCode(address.getAdminArea()));
            jsonObject.put("zipcode", address.getPostalCode());
        }catch (Exception e){
            Toast.makeText(Jaunt.getAppContext(), "Could not create json from address.", Toast.LENGTH_SHORT).show();
            jsonObject = null;
        }
        return jsonObject;
    }

    public static String toStateCode(String s){
        return Globals.states.get(s);
    }

    public static double distanceBetween(LatLng latLng1, LatLng latLng2){
        Location loc1 = new Location("");
        loc1.setLatitude(latLng1.latitude);
        loc1.setLongitude(latLng1.longitude);

        Location loc2 = new Location("");
        loc2.setLatitude(latLng2.latitude);
        loc2.setLongitude(latLng2.longitude);

        float distanceInMeters = loc1.distanceTo(loc2);
        return distanceInMeters;
    }

    //used to get accurate weather data, this will cause problem when the json is large
    //- tehrefore not used
    public static int getCityId(Address address){
        JSONArray jsonArray = JsonLoader.loadJsonArrayFromAsset("cities/city.list.json");
        int closestCityId = 0;
        double closestDistance = Double.MAX_VALUE;
        try{
            for (int i = 0; i<jsonArray.length(); i++) {
                JSONObject cityJson = (JSONObject)jsonArray.get(i);
                if(!address.getLocality().equalsIgnoreCase(cityJson.getString("name"))){
                    continue;
                }
                if(!address.getCountryCode().equalsIgnoreCase(cityJson.getString("country"))){
                    continue;
                }

                JSONObject coordJson = cityJson.getJSONObject("coord");
                double lat = coordJson.getDouble("lat");
                double lon = coordJson.getDouble("lon");

                double distance = distanceBetween(new LatLng(address.getLatitude(), address.getLongitude()), new LatLng(lat, lon));

                if(distance < closestDistance){
                    closestDistance = distance;
                    closestCityId = cityJson.getInt("id");
                }
            }
        }catch (JSONException e){
            Toast.makeText(Jaunt.getAppContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return closestCityId;
    }
}

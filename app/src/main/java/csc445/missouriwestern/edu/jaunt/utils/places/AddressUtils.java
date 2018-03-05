package csc445.missouriwestern.edu.jaunt.utils.places;

import android.location.Address;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Locale;

import csc445.missouriwestern.edu.jaunt.Globals;
import csc445.missouriwestern.edu.jaunt.Jaunt;

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
}

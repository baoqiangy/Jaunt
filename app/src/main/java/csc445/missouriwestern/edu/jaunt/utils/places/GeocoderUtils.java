package csc445.missouriwestern.edu.jaunt.utils.places;

import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

import csc445.missouriwestern.edu.jaunt.Jaunt;

/**
 * Created by byan on 3/1/2018.
 */

public class GeocoderUtils {

    public static Geocoder sharedGeocoder = new Geocoder(Jaunt.getAppContext(), Locale.getDefault());

    public static Address getAddressByCoordinates(Geocoder mGeocoder, LatLng latLng){

        try{
            List<Address> addresses = mGeocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                return addresses.get(0);
            }
            return null;
        }catch (Exception e){
            return null;
        }
    }

    public static Address getAddressByCoordinates(LatLng latLng){

        try{
            List<Address> addresses = sharedGeocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                return addresses.get(0);
            }
            return null;
        }catch (Exception e){
            return null;
        }
    }
}

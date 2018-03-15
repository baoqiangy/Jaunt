package csc445.missouriwestern.edu.jaunt.utils.userinfo;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import csc445.missouriwestern.edu.jaunt.Globals;
import csc445.missouriwestern.edu.jaunt.Jaunt;

/**
 * Created by byan on 3/15/2018.
 */

public class PreferencesWrapper {
    public static SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Jaunt.getAppContext());

    public static boolean isSignedIn(){
        return prefs.getBoolean("signed_in", false);
    }

    public static int getDriverId(){
        if(!isSignedIn()) return 0;
        return prefs.getInt("driverId", 0);
    }

    public static String getDriverEmail(){
        if(!isSignedIn()) return null;
        return prefs.getString("email", null);
    }

    public static String getBookName(){
        if(!isSignedIn()) return Globals.GUEST_BOOK;
        return "driver_" + prefs.getString("email", null);
    }
}

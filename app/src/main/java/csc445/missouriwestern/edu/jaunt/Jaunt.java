package csc445.missouriwestern.edu.jaunt;

import android.app.Application;
import android.content.Context;

import com.wonderkiln.blurkit.BlurKit;

import io.paperdb.Paper;

/**
 * Created by byan on 3/1/2018.
 */

public class Jaunt extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
//        if(PreferencesWrapper.getTimeZone() == null){
//            PreferencesWrapper.setTimeZone(TimeZone.getDefault());
//        }
        Jaunt.context = getApplicationContext();
        Paper.init(getApplicationContext());
        BlurKit.init(this);
    }

    public static Context getAppContext() {
        return Jaunt.context;
    }
}

package csc445.missouriwestern.edu.jaunt;

import android.app.Application;
import android.content.Context;

import io.paperdb.Paper;

/**
 * Created by byan on 3/1/2018.
 */

public class Jaunt extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        Jaunt.context = getApplicationContext();
        Paper.init(getApplicationContext());
    }

    public static Context getAppContext() {
        return Jaunt.context;
    }
}

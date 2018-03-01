package csc445.missouriwestern.edu.jaunt;

import android.app.Application;

import io.paperdb.Paper;

/**
 * Created by byan on 3/1/2018.
 */

public class Jaunt extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Paper.init(getApplicationContext());
    }
}

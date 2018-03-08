package csc445.missouriwestern.edu.jaunt.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by byan on 3/8/2018.
 */

public class RestaurantMenu implements Parcelable{
    int test;

    public int getTest() {
        return test;
    }

    public void setTest(int test) {
        this.test = test;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}

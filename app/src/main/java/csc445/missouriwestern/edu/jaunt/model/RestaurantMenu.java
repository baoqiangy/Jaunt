package csc445.missouriwestern.edu.jaunt.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by byan on 3/8/2018.
 */

public class RestaurantMenu implements Parcelable{

    List<FoodCategory> categories;

    public RestaurantMenu(List<FoodCategory> categories) {
        this.categories = categories;
    }

    protected RestaurantMenu(Parcel in) {

    }

    public static final Creator<RestaurantMenu> CREATOR = new Creator<RestaurantMenu>() {
        @Override
        public RestaurantMenu createFromParcel(Parcel in) {
            return new RestaurantMenu(in);
        }

        @Override
        public RestaurantMenu[] newArray(int size) {
            return new RestaurantMenu[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}

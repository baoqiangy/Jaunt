package csc445.missouriwestern.edu.jaunt.model;

import android.location.Address;
import android.util.Log;

import org.joda.time.DateTime;

import java.time.LocalDate;

/**
 * Created by byan on 2/22/2018.
 */

public class PlaceHistoryRecord {
    private DateTime lastSearchTime;
    private Address address;

    public PlaceHistoryRecord() {
    }

    public PlaceHistoryRecord(DateTime lastSearchTime, Address address) {
        this.lastSearchTime = lastSearchTime;
        this.address = address;
    }

    public DateTime getLastSearchTime() {
        return lastSearchTime;
    }

    public LocalDate getLastSearchJavaLocalDate(){
        LocalDate date = LocalDate.of(lastSearchTime.getYear(), lastSearchTime.getMonthOfYear(), lastSearchTime.getDayOfMonth());
        Log.d("PlaceHistoryRecord", date.toString());
        return date;
    }

    public void setLastSearchTime(DateTime lastSearchTime) {
        this.lastSearchTime = lastSearchTime;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}

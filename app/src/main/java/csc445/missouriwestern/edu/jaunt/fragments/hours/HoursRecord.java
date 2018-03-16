package csc445.missouriwestern.edu.jaunt.fragments.hours;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import csc445.missouriwestern.edu.jaunt.Jaunt;

/**
 * Created by byan on 2/17/2018.
 */

public class HoursRecord implements Parcelable{
    private int day;
    private List<TimeRange> availability;

    public HoursRecord(int day) {
        this.day = day;
    }

    public HoursRecord(int day, List<TimeRange> availability) {
        this.day = day;
        this.availability = availability;
    }

    public HoursRecord(JSONObject jsonObject) {
        try{
            this.day = jsonObject.getInt("day");
            this.availability = new ArrayList<>();
            if(!jsonObject.has("availability")){
                return;
            }
            JSONArray jsonArray = jsonObject.getJSONArray("availability");
            for (int i = 0; i < jsonArray.length(); i++) {
                availability.add( new TimeRange(jsonArray.getJSONObject(i)));
            }
        }catch (JSONException e){
            Toast.makeText(Jaunt.getAppContext(), "Converting JSONObject to HoursRecord 1 - " + e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    public HoursRecord(int day, JSONArray jsonArray){
        this.day = day;
        if(jsonArray == null) {
            return;
        }
        availability = new ArrayList<>();
        try{
            for (int i = 0; i < jsonArray.length(); i++) {
                availability.add( new TimeRange(jsonArray.getJSONObject(i)));
            }
        }catch (JSONException e){
            Toast.makeText(Jaunt.getAppContext(), "Converting JSONObject to HoursRecord 2 - " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    protected HoursRecord(Parcel in) {
        day = (Integer) in.readSerializable();
        availability = new ArrayList<>();
        in.readList(availability,TimeRange.class.getClassLoader());
        if(availability.size() == 0){
            availability = null;
        }
    }

    public static final Creator<HoursRecord> CREATOR = new Creator<HoursRecord>() {
        @Override
        public HoursRecord createFromParcel(Parcel in) {
            return new HoursRecord(in);
        }

        @Override
        public HoursRecord[] newArray(int size) {
            return new HoursRecord[size];
        }
    };

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public List<TimeRange> getAvailability() {
        return availability;
    }

    public void setAvailability(List<TimeRange> availability) {
        this.availability = availability;
    }

    public String getDayStr(){
        String result = "";
        switch (day) {
            case 1:
                result = "Monday";
                break;
            case 2:
                result = "Tuesday";
                break;
            case 3:
                result = "Wednesday";
                break;
            case 4:
                result = "Thursday";
                break;
            case 5:
                result = "Friday";
                break;
            case 6:
                result = "Saturday";
                break;
            case 0:
                result = "Sunday";
                break;
        }
        return result;
    }

    public String getRangesStr(){
        String result = "";
        if(availability==null){
            return result;
        }

        result = StringUtils.join(availability, "\n");
        return result;
    }

    public JSONObject toJson(){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("day", day);
            JSONArray jsonArray = new JSONArray();
            if(availability == null) {
                return jsonObject;
            }
            for(int i=0; i<availability.size(); i++){
                jsonArray.put(availability.get(i).toJson());
            }
            jsonObject.put("availability", jsonArray);
        }catch (JSONException e){
            Toast.makeText(Jaunt.getAppContext(), "Converting HoursRecord to JSONObject - " + e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
        return jsonObject;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(new Integer(day));
        dest.writeList(availability);
    }
}


package csc445.missouriwestern.edu.jaunt.fragments.hours;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import org.joda.time.LocalTime;
import org.json.JSONException;
import org.json.JSONObject;

import csc445.missouriwestern.edu.jaunt.Jaunt;
import csc445.missouriwestern.edu.jaunt.utils.date.TimeWrapper;

/**
 * Created by byan on 2/17/2018.
 */

public class TimeRange implements Parcelable{
    private LocalTime start;
    private LocalTime end;
    private boolean newRange;
    private boolean startChanged;
    private boolean endChanged;

    public boolean isEndChanged() {
        return endChanged;
    }

    public void setEndChanged(boolean endChanged) {
        this.endChanged = endChanged;
    }

    public TimeRange() {
    }

    public TimeRange(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }

    public TimeRange(JSONObject jsonObject){
        try{
            JSONObject startJsonObj = jsonObject.getJSONObject("start");
            JSONObject endJsonObj   = jsonObject.getJSONObject("end");
            this.start = new LocalTime(startJsonObj.getInt("hour"), startJsonObj.getInt("minute"));
            this.end = new LocalTime(endJsonObj.getInt("hour"), endJsonObj.getInt("minute"));
        }catch (JSONException e){
            Toast.makeText(Jaunt.getAppContext(), "Converting JSONObject to TimeRange - " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    protected TimeRange(Parcel in) {
        start   = (LocalTime) in.readSerializable();
        end     = (LocalTime) in.readSerializable();
    }

    public static final Creator<TimeRange> CREATOR = new Creator<TimeRange>() {
        @Override
        public TimeRange createFromParcel(Parcel in) {
            return new TimeRange(in);
        }

        @Override
        public TimeRange[] newArray(int size) {
            return new TimeRange[size];
        }
    };

    public LocalTime getStart() {
        return start;
    }

    public String getStartStr(){
        if(newRange && !startChanged) return "";
        return TimeWrapper.jodaTimeToStr(start);
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public String getEndStr(){
        if(newRange && !endChanged) return "";
        return TimeWrapper.jodaTimeToStr(end);
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    public boolean isNewRange() {
        return newRange;
    }

    public void setNewRange(boolean newRange) {
        this.newRange = newRange;
    }

    public boolean isStartChanged() {
        return startChanged;
    }

    public void setStartChanged(boolean startChanged) {
        this.startChanged = startChanged;
    }

    @Override
    public String toString() {
        return getStartStr() + " - " + getEndStr();
    }

    public JSONObject toJson(){
        JSONObject finalJsonObject  = new JSONObject();
        JSONObject startJsonObj     = new JSONObject();
        JSONObject endJsonObj       = new JSONObject();
        try{
            startJsonObj.put("hour", start.getHourOfDay());
            startJsonObj.put("minute", start.getMinuteOfHour());
            endJsonObj.put("hour", end.getHourOfDay());
            endJsonObj.put("minute", end.getMinuteOfHour());
            finalJsonObject.put("start", startJsonObj);
            finalJsonObject.put("end", endJsonObj);
        }catch (JSONException e){
            Toast.makeText(Jaunt.getAppContext(), "Converting TimeRange to JSONObject - " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return  finalJsonObject;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(start);
        dest.writeSerializable(end);
    }

}

package csc445.missouriwestern.edu.jaunt.utils.userinfo;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import csc445.missouriwestern.edu.jaunt.Globals;
import csc445.missouriwestern.edu.jaunt.Jaunt;
import csc445.missouriwestern.edu.jaunt.fragments.hours.HoursRecord;
import csc445.missouriwestern.edu.jaunt.fragments.hours.TimeRange;
import io.paperdb.Paper;

/**
 * Created by byan on 3/14/2018.
 */

public class PersistenceWrapper {
    public static String getBookName(){
        return PreferencesWrapper.getBookName();
    }

    public static void saveHoursRecords(List<HoursRecord> hoursRecords){
        if(hoursRecords == null) return;
        List<JSONObject> hoursRecordsJson = new ArrayList<>();
        for(int i=0; i<hoursRecords.size(); i++){
            hoursRecordsJson.add(hoursRecords.get(i).toJson());
        }
        String book_name = getBookName();
        Paper.book(book_name).write(Globals.AVAILABILITY_KEY, hoursRecordsJson);
    }

    //if the availability of some day is null, it is not included
    public static List<JSONObject> loadHoursAsJsonList(){
        List<HoursRecord> hoursRecords = loadHoursRecords();
        if(hoursRecords == null) return null;
        List<JSONObject> hoursRecordsJson = new ArrayList<>();
        for(int i=0; i<hoursRecords.size(); i++){
            List<TimeRange> availability = hoursRecords.get(i).getAvailability();
            if(availability == null || availability.size()==0)
                continue;
            hoursRecordsJson.add(hoursRecords.get(i).toJson());
        }
        return hoursRecordsJson;
    }

    public static void saveHoursRecordsJsonArray(JSONArray hoursRecordsJsonArray){
        if(hoursRecordsJsonArray == null) return;
        List<JSONObject> hoursRecordsJson = new ArrayList<>();
        try{
            for(int i=0; i<hoursRecordsJsonArray.length(); i++){
                hoursRecordsJson.add((JSONObject) hoursRecordsJsonArray.get(i));
            }
        }catch (JSONException e){
            Toast.makeText(Jaunt.getAppContext(), "Exception in saveHoursRecordsJsonArray - " + e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }
        String book_name = getBookName();
        Paper.book(book_name).write(Globals.AVAILABILITY_KEY, hoursRecordsJson);
    }

    public static List<HoursRecord>  loadHoursRecords(){
        String book_name = getBookName();
        List<JSONObject> hoursRecordsJson = Paper.book(book_name).read(Globals.AVAILABILITY_KEY);
        if(hoursRecordsJson == null) {
            return null;
        }
        List<HoursRecord> hoursRecords = new ArrayList<>();
        for(int i=0; i<hoursRecordsJson.size(); i++){
            hoursRecords.add(new HoursRecord(hoursRecordsJson.get(i)));
        }
        return hoursRecords;
    }
}

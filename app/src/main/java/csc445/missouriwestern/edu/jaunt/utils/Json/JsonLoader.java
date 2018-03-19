package csc445.missouriwestern.edu.jaunt.utils.Json;

import android.arch.persistence.room.Room;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import csc445.missouriwestern.edu.jaunt.Jaunt;
import csc445.missouriwestern.edu.jaunt.room.AppDatabase;
import csc445.missouriwestern.edu.jaunt.room.City;

/**
 * Created by byan on 3/17/2018.
 */

public class JsonLoader {

    //not memory efficient
    public static JSONArray loadJsonArrayFromAsset(String fileName){
        String json = null;
        JSONArray jsonArray = null;
        try {
            InputStream is = Jaunt.getAppContext().getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            jsonArray = new JSONArray(json);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
        return jsonArray;
    }
    public static JSONObject loadJsonObjectFromAsset(String fileName){
        String json = null;
        JSONObject jsonObject = null;
        try {
            InputStream is = Jaunt.getAppContext().getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            jsonObject = new JSONObject(json);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
        return jsonObject;
    }

    //memory efficient
    public List<City> readCityStream(InputStream in) throws IOException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        List<City> cities = new ArrayList<City>();
        reader.beginArray();
        while (reader.hasNext()) {
            City city = gson.fromJson(reader, City.class);
            cities.add(city);
        }
        reader.endArray();
        reader.close();
        return cities;
    }

    public void writeCityStream(OutputStream out, List<City> cities) throws IOException {
        Gson gson = new Gson();
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.setIndent("  ");
        writer.beginArray();
        for (City city : cities) {
            gson.toJson(city, City.class, writer);
        }
        writer.endArray();
        writer.close();
    }

    public static void readCityDom(String file) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            Gson gson = new GsonBuilder().create();
            City[] cities = gson.fromJson(reader, City[].class);

            System.out.println("Object mode: " + cities[0]);

        } catch (FileNotFoundException ex) {

        } finally {

        }
    }

    //this method cannot be called on main thread
    public static int readCitiesFromJsonFileToRoom(String pathToJsonFile){
        AppDatabase db = Room.databaseBuilder(Jaunt.getAppContext(),
                AppDatabase.class, "jaunt-database").build();
        Gson gson = new Gson();
        int count = db.cityDao().countCities();
        if(count > 0) {
            return count;
        }
        try{
            InputStream is = Jaunt.getAppContext().getAssets().open(pathToJsonFile);
            JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
            reader.beginArray();
            count = 0;
            while (reader.hasNext()) {
                City city = gson.fromJson(reader, City.class);
                db.cityDao().insertAll(city);
                count++;
            }
            reader.endArray();
            reader.close();

        }catch (IOException e){
            Toast.makeText(Jaunt.getAppContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            return 0;
        }
        return count;
    }
}

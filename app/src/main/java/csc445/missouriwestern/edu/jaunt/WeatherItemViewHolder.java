package csc445.missouriwestern.edu.jaunt;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;

import csc445.missouriwestern.edu.jaunt.utils.date.TimeWrapper;

/**
 * Created by byan on 3/15/2018.
 */

public class WeatherItemViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{

    private Context context;
    public View rootView;
    private TextView weekdayTextView;
    private ImageView weatherImage;
    private TextView weatherStatus;

    public JSONObject weatherForcast;
    private WeatherRecyclerViewAdapter.RecyclerViewItemOnClickedListener clickListener;

    public WeatherItemViewHolder(Context context, View view) {
        super(view);
        this.context = context;
        rootView = view;
        weekdayTextView = view.findViewById(R.id.weekday);
        weatherImage = view.findViewById(R.id.weather_img);
        weatherStatus = view.findViewById(R.id.weather_status);
    }

    void bindView(JSONObject weatherForcast, WeatherRecyclerViewAdapter.RecyclerViewItemOnClickedListener listener) {
        this.weatherForcast = weatherForcast;
        clickListener = listener;

        if(weatherForcast != null) {
            try {
                DateFormat df = DateFormat.getDateTimeInstance();
                String day = df.format(new Date(weatherForcast.getLong("dt")*1000));
                day = TimeWrapper.utcToString(weatherForcast.getLong("dt"), "h a");
                weekdayTextView.setText(day);
                Glide.with(context).load(Globals.OPEN_WEATHER_ICON_FOLDER_URL+weatherForcast.getJSONArray("weather").getJSONObject(0).getString("icon")+".png").into(weatherImage);
                weatherStatus.setText(weatherForcast.getJSONArray("weather").getJSONObject(0).getString("main") + "\n" + String.format("%.1f ℃", weatherForcast.getJSONObject("main").getDouble("temp")));
                //                cityField.setText(json.getString("name").toUpperCase(Locale.US) +
//                        ", " +
//                        json.getJSONObject("sys").getString("country"));
//
//                JSONObject details = json.getJSONArray("weather").getJSONObject(0);
//                JSONObject main = json.getJSONObject("main");
//                detailsField.setText(
//                        details.getString("description").toUpperCase(Locale.US) +
//                                "\n" + "Humidity: " + main.getString("humidity") + "%" +
//                                "\n" + "Pressure: " + main.getString("pressure") + " hPa");
//
//                currentTemperatureField.setText(
//                        String.format("%.2f", main.getDouble("temp"))+ " ℃");
//
//                DateFormat df = DateFormat.getDateTimeInstance();
//                String updatedOn = df.format(new Date(json.getLong("dt")*1000));
//                updatedField.setText("Last update: " + updatedOn);
//
//                setWeatherIcon(details.getInt("id"),
//                        json.getJSONObject("sys").getLong("sunrise") * 1000,
//                        json.getJSONObject("sys").getLong("sunset") * 1000);

            }catch(Exception e){
                Log.e("SimpleWeather", "One or more fields not found in the JSON data");
            }
        }
        rootView.setOnClickListener(this);
    }

    private void renderWeather(JSONObject json){
        try {
//            cityField.setText(json.getString("name").toUpperCase(Locale.US) +
//                    ", " +
//                    json.getJSONObject("sys").getString("country"));
//
//            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
//            JSONObject main = json.getJSONObject("main");
//            detailsField.setText(
//                    details.getString("description").toUpperCase(Locale.US) +
//                            "\n" + "Humidity: " + main.getString("humidity") + "%" +
//                            "\n" + "Pressure: " + main.getString("pressure") + " hPa");
//
//            currentTemperatureField.setText(
//                    String.format("%.2f", main.getDouble("temp"))+ " ℃");
//
//            DateFormat df = DateFormat.getDateTimeInstance();
//            String updatedOn = df.format(new Date(json.getLong("dt")*1000));
//            updatedField.setText("Last update: " + updatedOn);
//
//            setWeatherIcon(details.getInt("id"),
//                    json.getJSONObject("sys").getLong("sunrise") * 1000,
//                    json.getJSONObject("sys").getLong("sunset") * 1000);

        }catch(Exception e){
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }

    public void onClick(View v) {
        clickListener.recyclerViewItemClicked(this);
    }
}

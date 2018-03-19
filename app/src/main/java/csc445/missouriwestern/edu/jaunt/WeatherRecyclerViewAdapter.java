package csc445.missouriwestern.edu.jaunt;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by byan on 3/15/2018.
 */

public class WeatherRecyclerViewAdapter extends RecyclerView.Adapter<WeatherItemViewHolder> {

    private Context context;
    private JSONArray weatherForcasts;
    private WeatherRecyclerViewAdapter.RecyclerViewItemOnClickedListener listener;
    private String TAG = "TAG_WeatherRecyclerViewAdapter";

    public WeatherRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public WeatherRecyclerViewAdapter(Context context, JSONArray weatherForcasts) {
        this.context = context;
        this.weatherForcasts = weatherForcasts;
    }

    public void setListener(WeatherRecyclerViewAdapter.RecyclerViewItemOnClickedListener i){
        listener = i;
    }

    @Override
    public WeatherItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_weather, parent, false);
        WeatherItemViewHolder weatherItemViewHolder = new WeatherItemViewHolder(context, view);
        return weatherItemViewHolder;

    }

    @Override
    public void onBindViewHolder(WeatherItemViewHolder holder, int position) {
        try{
            JSONObject forcast = weatherForcasts.getJSONObject(position);
            // bind your view here
            // code moved to the holder class itself with a listener for click event
            holder.bindView(forcast, listener);
        }catch (JSONException e){
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return weatherForcasts==null ? 0 : weatherForcasts.length();
    }

    public interface RecyclerViewItemOnClickedListener{
        void recyclerViewItemClicked(RecyclerView.ViewHolder viewHolder);
    }
}

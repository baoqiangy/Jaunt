package csc445.missouriwestern.edu.jaunt.room;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import csc445.missouriwestern.edu.jaunt.Jaunt;
import csc445.missouriwestern.edu.jaunt.utils.Json.JsonLoader;

/**
 * Created by byan on 3/17/2018.
 */

public class CityTableInitializer extends AsyncTask {

    @Override
    protected Object doInBackground(Object[] objects) {
        int count = JsonLoader.readCitiesFromJsonFileToRoom("cities/city.list.json");
        return new Integer(count);
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Log.d("CitiTableInitializer", "Total records - " + ((Integer) o));
        Toast.makeText(Jaunt.getAppContext(), "Total records - " + ((Integer) o), Toast.LENGTH_LONG).show();
    }


}

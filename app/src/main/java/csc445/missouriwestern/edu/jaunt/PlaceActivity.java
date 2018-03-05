package csc445.missouriwestern.edu.jaunt;

/**
 * Created by byan on 2/20/2018.
 * Borrowed from https://stackoverflow.com/questions/15804805/android-action-bar-searchview-as-autocomplete
 */

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import csc445.missouriwestern.edu.jaunt.extensions.listeners.DrawableOnTouchListener;
import csc445.missouriwestern.edu.jaunt.extensions.ui.CustomAutoCompleteTextView;
import csc445.missouriwestern.edu.jaunt.fragments.places.EmptyFragment;
import csc445.missouriwestern.edu.jaunt.fragments.places.PlaceAutocompleteAdapter;
import csc445.missouriwestern.edu.jaunt.fragments.preplace.PreplaceFragment;
import csc445.missouriwestern.edu.jaunt.model.PlaceHistoryRecord;
import csc445.missouriwestern.edu.jaunt.utils.places.GeocoderUtils;
import io.paperdb.Paper;

public class PlaceActivity extends AppCompatActivity implements PreplaceFragment.OnDataPass,
        GoogleApiClient.OnConnectionFailedListener, AdapterView.OnItemClickListener{

    private CustomAutoCompleteTextView placeAutoCompleteTextView;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private GoogleApiClient mGoogleApiClient;
    private PreplaceFragment historyFragment;
    private EmptyFragment emptyFragment;
    private Class fragmentClass;
    private Geocoder mGeocoder;

    private ResultCallback<PlaceBuffer> callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        mGeocoder = new Geocoder(this, Locale.getDefault());
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupBackEventListener();
        setupPlaceAutoComplete();
        displaySearchHistory();
    }

    private void setupPlaceAutoComplete() {
        mGeoDataClient = Places.getGeoDataClient(this, null);
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_COUNTRY)
                .setCountry("US")
                .build();
        //placeAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGeoDataClient, LAT_LNG_BOUNDS, typeFilter);
        placeAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGeoDataClient, null, typeFilter);
        this.placeAutoCompleteTextView.setAdapter(placeAutocompleteAdapter);
        this.placeAutoCompleteTextView.setOnItemClickListener(this);
    }

    private void setupBackEventListener() {
        this.placeAutoCompleteTextView = (CustomAutoCompleteTextView) findViewById(R.id.placeAutoCompleteTextView);

        this.placeAutoCompleteTextView.setOnTouchListener(
                new DrawableOnTouchListener(this, this.placeAutoCompleteTextView, DrawableOnTouchListener.Position.START) {
                    @Override
                    public boolean onDrawableTouch(final MotionEvent event) {
                        return onClickBack(event);
                    }
                }
        );

        this.placeAutoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    clearTextForAutoTextView();
                    removeSearchHistory();
                }else{
                    displaySearchHistory();
                }
            }
        });
        this.placeAutoCompleteTextView.clearFocus();
    }

    private void clearTextForAutoTextView() {
        this.placeAutoCompleteTextView.setText("");
    }

    private boolean onClickBack(MotionEvent event) {
        finish();
        return true;
    }

    private void displaySearchHistory() {
        fragmentClass = PreplaceFragment.class;
        try{
            Fragment mFragment = (Fragment) fragmentClass.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.place_activity_fragment, mFragment).commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void removeSearchHistory(){
        fragmentClass = EmptyFragment.class;
        try{
            Fragment mFragment = (Fragment) fragmentClass.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.place_activity_fragment, mFragment).commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDataPass(String gms_id, Address data) {
        //Address selectedAddress = GeocoderUtils.getAddressByCoordinates(mGeocoder, data.getLatLng());

        //Log.d("Google Place - ", data.toString());
        //Log.d("Android Location - ", selectedAddress.toString());

        Intent returnIntent = new Intent();
        returnIntent.putExtra("place", data);
        returnIntent.putExtra("gms_id", gms_id);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection Lost " + connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    /**
     * OnItemSelected does not work. Use OnItemClick
     * */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AutocompletePrediction selectedPrediction = (AutocompletePrediction) parent.getItemAtPosition(position);
        Log.d("selectedPrediction", selectedPrediction.getFullText(null).toString());
        Places.GeoDataApi.getPlaceById(mGoogleApiClient, selectedPrediction.getPlaceId())
                .setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (places.getStatus().isSuccess()) {
                            final Place selectedPlace = places.get(0);
                            updatePlaceHistory(selectedPlace);
                            onDataPass(selectedPlace.getId(), GeocoderUtils.getAddressByCoordinates(mGeocoder, selectedPlace.getLatLng()));
                        }
                        places.release();
                    }
                });
    }

    public void updatePlaceHistory(Place place){
        LatLng queriedLocation = place.getLatLng();
        Log.v("Latitude is", "" + queriedLocation.latitude);
        Log.v("Longitude is", "" + queriedLocation.longitude);
        try{
            List<PlaceHistoryRecord> currentRecords = Paper.book().read(Globals.PLACE_HISTORY_KEY, new ArrayList<PlaceHistoryRecord>());
            DateTime currentTime = new DateTime(DateTimeZone.getDefault());
            if(currentRecords == null) {
                currentRecords = new ArrayList<>();
                currentRecords.add(new PlaceHistoryRecord(currentTime, place.getId(), GeocoderUtils.getAddressByCoordinates(mGeocoder, queriedLocation)));
            }else{
                boolean found = false;
                for (PlaceHistoryRecord element : currentRecords) {
                    if(element.getAddress().equals(place)){
                        element.setLastSearchTime(currentTime);
                        found = true;
                        break;
                    }
                }
                if(!found){
                    currentRecords.add(new PlaceHistoryRecord(currentTime, place.getId(), GeocoderUtils.getAddressByCoordinates(mGeocoder, queriedLocation)));
                }
            }

            currentRecords.sort(new Comparator<PlaceHistoryRecord>() {
                @Override
                public int compare(PlaceHistoryRecord lhs, PlaceHistoryRecord rhs) {
                    // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                    return lhs.getLastSearchTime().isAfter(rhs.getLastSearchTime()) ? 1 : 0;
                }
            });

            Paper.book().write(Globals.PLACE_HISTORY_KEY, currentRecords);
        }catch (Exception e){
            Toast.makeText(this, "Could not read search history.", Toast.LENGTH_SHORT).show();
        }

    }
}

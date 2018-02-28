package csc445.missouriwestern.edu.jaunt;

/**
 * Created by byan on 2/20/2018.
 * Borrowed from https://stackoverflow.com/questions/15804805/android-action-bar-searchview-as-autocomplete
 */

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import csc445.missouriwestern.edu.jaunt.extensions.listeners.DrawableOnTouchListener;
import csc445.missouriwestern.edu.jaunt.extensions.ui.CustomAutoCompleteTextView;
import csc445.missouriwestern.edu.jaunt.fragments.places.PlacesFragment;
import csc445.missouriwestern.edu.jaunt.fragments.preplace.PreplaceFragment;

public class PlaceActivity extends AppCompatActivity implements PreplaceFragment.OnDataPass{

    private CustomAutoCompleteTextView placeAutoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        displaySearchHistory();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupBackEventListener();
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
                    displayPlacePredictions();
                }
            }
        });
        this.placeAutoCompleteTextView.clearFocus();
    }

    private void displayPlacePredictions() {
        PlacesFragment placesFragment = new PlacesFragment();
        //This is the new fragment manager instead of the one from v4
        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.place_activity_fragment, placesFragment).commit();

        //PlaceAutocompleteFragment autocompleteFragment;
        //cannot use the above fragment manager
        //autocompleteFragment = (PlaceAutocompleteFragment) placesFragment.getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
                //this.getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

    }

    private boolean onClickBack(MotionEvent event) {
        finish();
        return true;
    }

    private void displaySearchHistory() {
        PreplaceFragment historyFragment = new PreplaceFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.place_activity_fragment, historyFragment).commit();
    }

    @Override
    public void onDataPass(Address data) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("place", data);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}

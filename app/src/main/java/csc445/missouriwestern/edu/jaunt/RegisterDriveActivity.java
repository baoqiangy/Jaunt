package csc445.missouriwestern.edu.jaunt;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import csc445.missouriwestern.edu.jaunt.extensions.ui.CustomTextInputLayout;
import csc445.missouriwestern.edu.jaunt.model.Driver;
import io.paperdb.Paper;

public class RegisterDriveActivity extends AppCompatActivity {

    private EditText firstNameInput;
    private EditText lastNameInput;
    private CustomTextInputLayout emailInputLayout;
    private EditText emailInput;
    private EditText birthdayInput;
    private EditText address1Input;
    private EditText cityStateInput;
    private EditText zipcodeInput;
    private EditText driverLicenseInput;
    private ConstraintLayout driverRegisterLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_drive);
        driverRegisterLayout = findViewById(R.id.driver_register_layout);
        emailInputLayout = findViewById(R.id.email);
        emailInput = findViewById(R.id.email_input);
        firstNameInput = findViewById(R.id.first_name_input);
        lastNameInput = findViewById(R.id.last_name_input);
        populateFieldsWithDriverInfoIfLoggedIn();
        setupBirthdayPicker();
        setupAddressPicker();
        setupDriverLicensePicker();
        driverRegisterLayout.requestFocus();
    }

    private void populateFieldsWithDriverInfoIfLoggedIn() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!prefs.getBoolean("signed_in", false)){
            return;
        }
        Driver driver = Paper.book("driver_"+prefs.getString("email", null)).read(Globals.ACCOUNT_INFO_KEY);
        if(driver == null){
            return;
        }
        firstNameInput.setText(driver.getFirstName());
        lastNameInput.setText(driver.getLastName());
        emailInput.setText(driver.getEmail());
    }

    private void setupDriverLicensePicker() {
        driverLicenseInput = findViewById(R.id.driver_license_input);
        driverLicenseInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    Intent displayLicensePicker = new Intent(RegisterDriveActivity.this, ImageActivity.class);
                    startActivityForResult(displayLicensePicker, 166);
                }
            }
        });
    }

    private void setupAddressPicker() {
        address1Input = findViewById(R.id.address1_input);
        cityStateInput = findViewById(R.id.city_state_input);
        zipcodeInput = findViewById(R.id.zipcode_input);

        address1Input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    Intent displayPlacePicker = new Intent(RegisterDriveActivity.this, PlaceActivity.class);
                    startActivityForResult(displayPlacePicker, 167);
                }
            }
        });
    }

    public void setupBirthdayPicker(){
        birthdayInput = findViewById(R.id.birthday_input);
        birthdayInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    Intent displayCalendar = new Intent(RegisterDriveActivity.this, CalendarActivity.class);
                    startActivityForResult(displayCalendar, 168);
                }
            }
        });
    }

    public void registerButtonClicked(View view) {
        Toast.makeText(RegisterDriveActivity.this, "Entered " + emailInput.getText(), Toast.LENGTH_SHORT).show();
    }

    /***
     * Passing object between activities
     * https://stackoverflow.com/questions/2139134/how-to-send-an-object-from-one-android-activity-to-another-using-intents
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 166) {
            if(resultCode == Activity.RESULT_OK){
                String selectedPhotoPath = data.getParcelableExtra("photo");
                driverLicenseInput.setText(selectedPhotoPath);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }

        if (requestCode == 167) {
            if(resultCode == Activity.RESULT_OK){
                //Place selectedPlace = (Place) data.getParcelableExtra("place");
                //Geocoder mGeocoder = new Geocoder(this, Locale.getDefault());
                //Address selectedAddress = GeocoderUtils.getAddressByCoordinates(mGeocoder, selectedPlace.getLatLng());
                Address selectedAddress = (Address) data.getParcelableExtra("place");
                address1Input.setText(selectedAddress.getAddressLine(0));
                cityStateInput.setText(selectedAddress.getLocality() + "/" + selectedAddress.getAdminArea());
                String zipcode = selectedAddress.getPostalCode();
                zipcodeInput.setText((zipcode!=null)?zipcode:"");
                driverRegisterLayout.requestFocus();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
            //address1Input.requestFocus();
            //birthdayInput.clearFocus();
        }

        if (requestCode == 168) {
            if(resultCode == Activity.RESULT_OK){
                String selectedDate = data.getStringExtra("date");
                birthdayInput.setText(selectedDate);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
            //address1Input.requestFocus();
            //birthdayInput.clearFocus();
        }
    }
}

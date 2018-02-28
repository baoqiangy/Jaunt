package csc445.missouriwestern.edu.jaunt;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import csc445.missouriwestern.edu.jaunt.extensions.ui.CustomTextInputLayout;

public class RegisterDriveActivity extends AppCompatActivity {

    private CustomTextInputLayout emailInputLayout;
    private EditText emailInput;
    private EditText birthdayInput;
    private EditText address1Input;
    private EditText cityStateInput;
    private EditText zipcodeInput;
    private EditText driverLicenseInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_drive);
        emailInputLayout = findViewById(R.id.email);
        emailInput = findViewById(R.id.email_input);
        setupBirthdayPicker();
        setupAddressPicker();
        setupDriverLicensePicker();
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
                Address selectedAddress = (Address) data.getParcelableExtra("place");
                address1Input.setText(selectedAddress.getAddressLine(0));
                cityStateInput.setText(selectedAddress.getSubLocality() + "/" + selectedAddress.getLocality());
                String zipcode = selectedAddress.getPostalCode();
                zipcodeInput.setText((zipcode!=null)?zipcode:"");
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

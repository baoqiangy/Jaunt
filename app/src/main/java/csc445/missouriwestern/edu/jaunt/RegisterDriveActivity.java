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

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.IpCons;
import com.esafirm.imagepicker.model.Image;

import java.util.List;

import csc445.missouriwestern.edu.jaunt.extensions.ui.CustomTextInputLayout;
import csc445.missouriwestern.edu.jaunt.model.Driver;
import csc445.missouriwestern.edu.jaunt.thirdparty.imagepicker.ImagePickerWrapper;
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
                    ImagePicker imagePicker = ImagePicker.create(RegisterDriveActivity.this);
                    ImagePickerWrapper.pickImage(imagePicker);
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
        if(resultCode == Activity.RESULT_OK){
            if (ImagePicker.shouldHandle(IpCons.RC_IMAGE_PICKER, resultCode, data)) {
                // Get a list of picked images
                List<Image> images = ImagePicker.getImages(data);
                // or get a single image only
                Image image = ImagePicker.getFirstImageOrNull(data);
                if(image != null)
                    driverLicenseInput.setText(images.get(0).getPath());
                //update the image when save button is clicked
            }

            if (requestCode == 167) {
                Address selectedAddress = (Address) data.getParcelableExtra("place");
                address1Input.setText(selectedAddress.getAddressLine(0));
                cityStateInput.setText(selectedAddress.getLocality() + "/" + selectedAddress.getAdminArea());
                String zipcode = selectedAddress.getPostalCode();
                zipcodeInput.setText((zipcode!=null)?zipcode:"");
                driverRegisterLayout.requestFocus();
            }

            if (requestCode == 168) {
                String selectedDate = data.getStringExtra("date");
                birthdayInput.setText(selectedDate);
            }
        }

        if (resultCode == Activity.RESULT_CANCELED) {
            //Write your code if there's no result
        }

    }
}

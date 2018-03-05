package csc445.missouriwestern.edu.jaunt;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.IpCons;
import com.esafirm.imagepicker.model.Image;
import com.spothero.emailvalidator.EmailValidationResult;
import com.spothero.emailvalidator.EmailValidator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import csc445.missouriwestern.edu.jaunt.extensions.ui.CustomTextInputLayout;
import csc445.missouriwestern.edu.jaunt.model.Driver;
import csc445.missouriwestern.edu.jaunt.thirdparty.imagepicker.ImagePickerWrapper;
import csc445.missouriwestern.edu.jaunt.utils.date.DateWrapper;
import csc445.missouriwestern.edu.jaunt.utils.places.AddressUtils;
import io.paperdb.Paper;

public class RegisterDriveActivity extends AppCompatActivity {

    private CustomTextInputLayout firstNameInputLayout;
    private CustomTextInputLayout lastNameInputLayout;
    private CustomTextInputLayout emailInputLayout;
    private CustomTextInputLayout birthdayInputLayout;
    private CustomTextInputLayout phoneInputLayout;
    private CustomTextInputLayout address1InputLayout;
    private CustomTextInputLayout cityStateInputLayout;
    private CustomTextInputLayout zipcodeInputLayout;
    private CustomTextInputLayout driverLicenseInputLayout;

    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText emailInput;
    private EditText birthdayInput;
    private EditText address1Input;
    private EditText cityStateInput;
    private EditText zipcodeInput;
    private EditText phoneInput;
    private EditText driverLicenseInput;
    private ConstraintLayout driverRegisterLayout;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String birthday;
    private String address1;
    private String city;
    private String state;
    private String zipcode;

    private Driver driver;
    private Address selectedAddress;
    private String gms_id;

    private String TAG = "TAG_RegisterDriveActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_drive);
        wireUpInputs();
        setupBirthdayPicker();
        setupAddressPicker();
        setupDriverLicensePicker();
        populateFieldsWithDriverInfoIfSignedIn();
        driverRegisterLayout.requestFocus();
    }

    private void wireUpInputs() {
        driverRegisterLayout = findViewById(R.id.driver_register_layout);
        firstNameInputLayout = findViewById(R.id.first_name);
        lastNameInputLayout = findViewById(R.id.last_name);
        emailInputLayout = findViewById(R.id.email);
        phoneInputLayout = findViewById(R.id.phone);
        birthdayInputLayout = findViewById(R.id.birthday);
        address1InputLayout = findViewById(R.id.address1);
        cityStateInputLayout = findViewById(R.id.city_state);
        zipcodeInputLayout = findViewById(R.id.zipcode);
        driverLicenseInputLayout = findViewById(R.id.driver_license);

        emailInput = findViewById(R.id.email_input);
        firstNameInput = findViewById(R.id.first_name_input);
        lastNameInput = findViewById(R.id.last_name_input);
        phoneInput = findViewById(R.id.phone_input);
        birthdayInput = findViewById(R.id.birthday_input);
        address1Input = findViewById(R.id.address1_input);
        cityStateInput = findViewById(R.id.city_state_input);
        zipcodeInput = findViewById(R.id.zipcode_input);
        driverLicenseInput = findViewById(R.id.driver_license_input);
    }

    private void populateFieldsWithDriverInfoIfSignedIn() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean signed_in = prefs.getBoolean("signed_in", false);
        if(!signed_in){
            return;
        }
        String email = prefs.getString("email", null);
        String book_name = "driver_"+email;
        driver = Paper.book(book_name).read(Globals.ACCOUNT_INFO_KEY);
        if(driver == null){
            return;
        }
        firstNameInput.setText(driver.getFirstName());
        lastNameInput.setText(driver.getLastName());
        emailInput.setText(driver.getEmail());
        phoneInput.setText(driver.getPhone()!=null ? driver.getPhone() : "");
        birthdayInput.setText(driver.getBirthday() != null ? DateWrapper.dateToString(driver.getBirthday()) : "");

        Address address = driver.getAddress();
        if(address != null) {
            address1Input.setText(address.getAddressLine(0));
            cityStateInput.setText(address.getLocality() + "/" + AddressUtils.toStateCode(address.getAdminArea()));
            String zipcode = address.getPostalCode();
            zipcodeInput.setText((zipcode!=null)?zipcode:"");
        }
        driverLicenseInput.setText(driver.isHasLicensePhoto() ? "Yes, uploaded." :"Not uploaded yet.");
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

    private boolean validateInputs() {
        firstName = firstNameInput.getText().toString().trim();
        boolean validFirstName  = firstName.length() > 0;
        if(!validFirstName) {firstNameInputLayout.setError("First name is required");}

        lastName = lastNameInput.getText().toString().trim();
        boolean validLastName   = lastName.length() > 0;
        if(!validLastName) {lastNameInputLayout.setError("Last name is required");}

        email = emailInput.getText().toString().trim();
        boolean validEmail      = validateEmail(email);

        phone = phoneInput.getText().toString().trim();
        birthday = birthdayInput.getText().toString().trim();

        return validFirstName && validLastName && validEmail;
    }

    private boolean validateEmail(String email) {
        EmailValidationResult.ValidationError error = EmailValidator.validateSyntax(email);
        if(error == null) {
            return true;
        }
        switch (error){
            case BLANK_ADDRESS:
                emailInputLayout.setError("No email specified");
                break;
            case INVALID_SYNTAX:
                emailInputLayout.setError("Email is invalid");
                break;
            case INVALID_USERNAME:
                emailInputLayout.setError("Username is invalid");
                break;
            case INVALID_DOMAIN:
                emailInputLayout.setError("Email domain is invalid");
                break;
            case INVALID_TLD:
                emailInputLayout.setError("Top level domain is invalid");
                break;
            default:
                break;
        }
        return false;
    }

    public void submitButtonClicked(View view) {
        if(validateInputs()){
            updateDriver();
        }
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
                selectedAddress = (Address) data.getParcelableExtra("place");
                gms_id = data.getStringExtra("gms_id");
                address1Input.setText(selectedAddress.getAddressLine(0));
                cityStateInput.setText(selectedAddress.getLocality() + "/" + AddressUtils.toStateCode(selectedAddress.getAdminArea()));
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

    private void updateDriver(){
        try{
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String script = "update_driver.php";
            JSONObject params = new JSONObject();
            params.put("driver_id", driver != null ? driver.getDriverId() : 0);
            params.put("firstname", firstName);
            params.put("lastname", lastName);
            params.put("email", email);
            if(phone != null && phone.length() > 0){
                params.put("phone", phone);
            }

            if(birthday != null && birthday.length() > 0){
                params.put("birthday", DateWrapper.dateToSqlString(DateWrapper.stringToDate(birthday)));
            }
            if(driver != null){
                if(driver.getAddress() == null){
                    if(selectedAddress != null) {
                        JSONObject addressJson = AddressUtils.addressToJson(selectedAddress, gms_id);
                        params.put("address", addressJson);
                    }
                }else{
                    if(selectedAddress != null) {
                        if(driver.getGms_id() != null){
                            if(!driver.getGms_id().equalsIgnoreCase(gms_id)){
                                JSONObject addressJson = AddressUtils.addressToJson(selectedAddress, gms_id);
                                params.put("address", addressJson);
                            }
                        }
                    }
                }
            }

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    Globals.SERVER_URL + "/" + script,
                    params,
                    createListener_updateDriver(),
                    createErrorListener_updateDriver());

            //progressTxtView.setText("Processing ...");
            requestQueue.add(jsonObjectRequest);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private Response.Listener<JSONObject> createListener_updateDriver(){
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //progressTxtView.setText("Processing ... Done !");
                try{
                    int success = response.getInt("success");
                    if(success == 1) {
                        String new_address_id = null;
                        if(response.has("address_id")){
                            new_address_id = response.getString("address_id");
                        }
                        driver.setFirstName(firstName);
                        driver.setLastName(lastName);
                        driver.setEmail(email);
                        driver.setPhone(phone);
                        driver.setBirthday(DateWrapper.stringToDate(birthday));
                        if(new_address_id != null){
                            driver.setAddressId(new_address_id);
                        }
                        driver.setGms_id(gms_id);
                        driver.setAddress(selectedAddress);

                        String book_name = "driver_"+email;
                        Paper.book(book_name).write(Globals.ACCOUNT_INFO_KEY, driver);
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(RegisterDriveActivity.this);
                        prefs.edit().putString("email", email).commit();
                        Toast.makeText(RegisterDriveActivity.this, "Update Successful!", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Log.d(TAG, response.getString("error_message"));
                        Toast.makeText(RegisterDriveActivity.this, response.getString("error_message"), Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(RegisterDriveActivity.this, "Exception occurred." + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private Response.ErrorListener createErrorListener_updateDriver(){
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressTxtView.setText("Processing ... Error!");
                Log.d(TAG, error.getMessage());
                Toast.makeText(RegisterDriveActivity.this, "Error response from server - " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }

}

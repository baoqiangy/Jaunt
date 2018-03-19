package csc445.missouriwestern.edu.jaunt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.spothero.emailvalidator.EmailValidationResult;
import com.spothero.emailvalidator.EmailValidator;

import org.json.JSONException;
import org.json.JSONObject;

import csc445.missouriwestern.edu.jaunt.extensions.ui.CustomTextInputLayout;
import csc445.missouriwestern.edu.jaunt.model.Driver;
import csc445.missouriwestern.edu.jaunt.room.AppDatabase;
import csc445.missouriwestern.edu.jaunt.room.City;
import csc445.missouriwestern.edu.jaunt.room.CityTableInitializer;
import csc445.missouriwestern.edu.jaunt.utils.fonts.FontChangeCrawler;
import csc445.missouriwestern.edu.jaunt.utils.userinfo.PersistenceWrapper;
import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private ImageView imageName;
    private Driver driver;
    private CustomTextInputLayout emailEditTextLayout;
    private CustomTextInputLayout passwordEditTextLayout;
    private EditText emailEditText;
    private EditText passwordEditText;
    private String email;
    private String password;

    private String TAG = "TAG_LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        displayAppNameImage();
        wireUpInputFields();
    }

    private void wireUpInputFields() {
        emailEditTextLayout = findViewById(R.id.email_layout);
        passwordEditTextLayout = findViewById(R.id.password_layout);
        emailEditText = findViewById(R.id.input_email);
        passwordEditText = findViewById(R.id.input_password);
        //initializeRoomDatabase();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "AvenirNextLTPro-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        boolean signed_in = prefs.getBoolean("signed_in", false);
        Log.d(TAG, "signed_in - " + signed_in);
        if(signed_in){
            goto_account();
        }else{
            emailEditText.getText().clear();
            passwordEditText.getText().clear();
        }
    }

    private void displayAppNameImage() {
        imageName = findViewById(R.id.app_name);

        final ContextThemeWrapper wrapper = new ContextThemeWrapper(this, R.style.AppNameImageNewColor);
        final Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_jaunttitle, wrapper.getTheme());
        imageName.setImageDrawable(drawable);
    }

    public void signUpClicked(View view) {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }

    public void loginClicked(View view) {
        boolean validEmail = validateEmail(emailEditText.getText().toString().trim());
        boolean hasPassword = validatePassword();
        if(validEmail && hasPassword) {
            authenticate();
        }
    }

    private boolean validatePassword() {
        String passwordInput = passwordEditText.getText().toString().trim();

        if(passwordInput == null || passwordInput.length()==0){
            passwordEditTextLayout.setError("Password required");
            return false;
        }else{
            this.password = passwordInput;
        }
        passwordEditTextLayout.setError(null);
        return true;
    }

    private boolean validateEmail(String email) {
        EmailValidationResult.ValidationError error = EmailValidator.validateSyntax(email);
        if(error == null) {
            this.email = email;
            emailEditTextLayout.setError(null);
            return true;
        }
        switch (error){
            case BLANK_ADDRESS:
                emailEditTextLayout.setError("No email specified");
                break;
            case INVALID_SYNTAX:
                emailEditTextLayout.setError("Email is invalid");
                break;
            case INVALID_USERNAME:
                emailEditTextLayout.setError("Username is invalid");
                break;
            case INVALID_DOMAIN:
                emailEditTextLayout.setError("Email domain is invalid");
                break;
            case INVALID_TLD:
                emailEditTextLayout.setError("Top level domain is invalid");
                break;
            default:
                break;
        }
        return false;
    }

    private void authenticate(){
        try{
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String script = "login_driver.php";
            JSONObject params = new JSONObject();
            params.put("email", email);
            params.put("password", password);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    Globals.SERVER_URL + "/" + script,
                    params,
                    createListener_authenticate(),
                    createErrorListener_authenticate());

            //progressTxtView.setText("Processing ...");
            requestQueue.add(jsonObjectRequest);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private Response.Listener<JSONObject> createListener_authenticate(){
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //progressTxtView.setText("Processing ... Done !");
                try{
                    int success = response.getInt("success");
                    if(success == 1) {

                        JSONObject jsonObject = response.getJSONObject("driver_profile");
                        Driver me = new Driver(jsonObject);
                        PersistenceWrapper.saveHoursRecordsJsonArray(jsonObject.getJSONArray("hours_json"));
                        String book_name = "driver_"+email;
                        Paper.book(book_name).write(Globals.ACCOUNT_INFO_KEY, me);
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                        prefs.edit().putBoolean("signed_in", true).putString("email", email).putInt("driverId", me.getDriverId()).commit();
                        //prefs.edit().remove("email").putBoolean("signed_in", false).commit();
                        goto_account();
                    }else{
                        Log.d(TAG, response.getString("error_message"));
                        Toast.makeText(LoginActivity.this, response.getString("error_message"), Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "Exception occurred." + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void goto_account() {
        Intent intent = new Intent(LoginActivity.this, AccountActivity.class);
        startActivity(intent);
    }

    private Response.ErrorListener createErrorListener_authenticate(){
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressTxtView.setText("Processing ... Error!");
                Log.d(TAG, error.getMessage());
                Toast.makeText(LoginActivity.this, "Error response from server - " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void forgotPasswordClicked(View view) {
        Toast.makeText(LoginActivity.this, "Forgot password clicked.", Toast.LENGTH_SHORT).show();
    }

    private void initializeRoomDatabase(){
        CityTableInitializer initializer = new CityTableInitializer();
        initializer.execute();
    }

    private static City addCity(final AppDatabase db, City city) {
        db.cityDao().insertAll(city);
        return city;
    }
}

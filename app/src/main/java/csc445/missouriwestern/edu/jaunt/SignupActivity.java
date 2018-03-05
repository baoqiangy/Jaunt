package csc445.missouriwestern.edu.jaunt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
import io.paperdb.Paper;

public class SignupActivity extends AppCompatActivity {

    private CustomTextInputLayout firstNameEditTextLayout;
    private CustomTextInputLayout lastNameEditTextLayout;
    private CustomTextInputLayout emailEditTextLayout;
    private CustomTextInputLayout password1EditTextLayout;
    private CustomTextInputLayout password2EditTextLayout;

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText password1EditText;
    private EditText password2EditText;

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private String TAG = "TAG_SignupActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        TextView titleTextView = findViewById(R.id.register_title);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/AvenirNextCondensed-Regular.ttf");
        titleTextView.setTypeface(custom_font);
        wireUpInputFields();
    }

    private void wireUpInputFields() {
        firstNameEditTextLayout   = findViewById(R.id.first_name_layout);
        lastNameEditTextLayout    = findViewById(R.id.last_name_layout);
        emailEditTextLayout       = findViewById(R.id.email_layout);
        password1EditTextLayout   = findViewById(R.id.password1_layout);
        password2EditTextLayout   = findViewById(R.id.password2_layout);

        firstNameEditText   = findViewById(R.id.first_name);
        lastNameEditText    = findViewById(R.id.last_name);
        emailEditText       = findViewById(R.id.email);
        password1EditText   = findViewById(R.id.password1);
        password2EditText   = findViewById(R.id.password2);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SignupActivity.this);
        boolean signed_in = prefs.getBoolean("signed_in", false);
        Log.d(TAG, "signed_in - " + signed_in);
        if(signed_in){
            goto_account();
        }
    }

    public void signUpButtonClicked(View view) {
        if(!validateInput()){
            return;
        }
        signUp();
    }

    private boolean validateInput() {
        firstName = firstNameEditText.getText().toString().trim();
        boolean validFirstName  = firstName.length() > 0;
        if(!validFirstName) {firstNameEditTextLayout.setError("First name is required");}
        lastName = lastNameEditText.getText().toString().trim();
        boolean validLastName   = lastName.length() > 0;
        if(!validLastName) {lastNameEditTextLayout.setError("Last name is required");}

        email = emailEditText.getText().toString().trim();
        boolean validEmail      = validateEmail(email);

        boolean validPassword   = validatePasswords();
        return validFirstName && validLastName && validEmail && validPassword;
    }

    private boolean validatePasswords() {
        String password1 = password1EditText.getText().toString().trim();
        String password2 = password1EditText.getText().toString().trim();
        boolean matched = (password1.equals(password2));
        if(!matched){
            password2EditTextLayout.setError("Passwords do not match");
        }else{
            password = password1;
        }
        return matched;
    }

    private boolean validateEmail(String email) {
        EmailValidationResult.ValidationError error = EmailValidator.validateSyntax(email);
        if(error == null) {
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

    private void signUp(){
        try{
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String script = "create_driver.php";
            JSONObject params = new JSONObject();
            params.put("firstname", firstName);
            params.put("lastname", lastName);
            params.put("email", email);
            params.put("password", password);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    Globals.SERVER_URL + "/" + script,
                    params,
                    createListener_signUp(),
                    createErrorListener_signUp());

            //progressTxtView.setText("Processing ...");
            requestQueue.add(jsonObjectRequest);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private Response.Listener<JSONObject> createListener_signUp(){
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //progressTxtView.setText("Processing ... Done !");
                try{
                    int success = response.getInt("success");
                    if(success == 1) {
                        int driver_id = response.getInt("driver_id");
                        Driver me = new Driver(driver_id, firstName, lastName, email);
                        Paper.book("driver_"+email).write(Globals.ACCOUNT_INFO_KEY, me);
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SignupActivity.this);
                        prefs.edit().putBoolean("signed_in", true).putString("email", email).commit();
                        goto_account();
                    }else{
                        Log.d(TAG, response.getString("error_message"));
                        Toast.makeText(SignupActivity.this, response.getString("error_message"), Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void goto_account() {
        Intent intent = new Intent(SignupActivity.this, AccountActivity.class);
        startActivity(intent);
    }

    private Response.ErrorListener createErrorListener_signUp(){
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressTxtView.setText("Processing ... Error!");
                Log.d(TAG, error.getMessage());
            }
        };
    }
}

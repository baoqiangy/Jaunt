package csc445.missouriwestern.edu.jaunt;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
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

import java.util.Date;

import csc445.missouriwestern.edu.jaunt.utils.date.TimeWrapper;

public class ResetPasswordActivity extends AppCompatActivity {

    private ConstraintLayout codeSendLayout;
    private EditText registeredEmail;
    private String email;
    private EditText registeredPhone;
    private String phone;

    private ConstraintLayout codeVerificationLayout;
    private EditText digit1;
    private EditText digit2;
    private EditText digit3;
    private EditText digit4;
    private EditText digit5;
    private EditText digit6;

    private ConstraintLayout editPasswordLayout;
    private EditText newPassword1EditText;
    private EditText newPassword2EditText;
    private String newPassword1;
    private String newPassword2;

    private String verificationCode;
    private SharedPreferences prefs;
    private Button updatePasswordBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        prefs = PreferenceManager.getDefaultSharedPreferences(ResetPasswordActivity.this);
        wireupDigitInputs();
        hookupListeners();
        codeSendLayout.setVisibility(View.VISIBLE);
        codeVerificationLayout.setVisibility(View.INVISIBLE);
        editPasswordLayout.setVisibility(View.INVISIBLE);
    }

    private void wireupDigitInputs() {
        registeredEmail = findViewById(R.id.registered_email);
        registeredPhone = findViewById(R.id.registered_phone);

        digit1 = findViewById(R.id.code_digit1);
        digit2 = findViewById(R.id.code_digit2);
        digit3 = findViewById(R.id.code_digit3);
        digit4 = findViewById(R.id.code_digit4);
        digit5 = findViewById(R.id.code_digit5);
        digit6 = findViewById(R.id.code_digit6);

        newPassword1EditText = findViewById(R.id.input_newpassword1);
        newPassword2EditText = findViewById(R.id.input_newpassword2);

        codeSendLayout = findViewById(R.id.verification_send_include);
        codeVerificationLayout = findViewById(R.id.verification_input_include); //.findViewById(R.id.verification_layout)
        editPasswordLayout = findViewById(R.id.edit_password_include);

        updatePasswordBtn = findViewById(R.id.newpassword_submit);
    }

    private void hookupListeners(){
        createTextWatcherFor(digit1);
        createTextWatcherFor(digit2);
        createTextWatcherFor(digit3);
        createTextWatcherFor(digit4);
        createTextWatcherFor(digit5);
        createTextWatcherFor(digit6);
        if(updatePasswordBtn != null){
            updatePasswordBtn.setOnClickListener(createListener_updatePasswordBtn());
        }
    }

    private void createTextWatcherFor(EditText editText){
        int id = editText.getId();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 0) {
                    if(start == 0 && before == 1){
                        //digit deleted
                        switch (id) {
                            case R.id.code_digit6:
                                digit5.requestFocus();
                                break;
                            case R.id.code_digit5:
                                digit4.requestFocus();
                                break;
                            case R.id.code_digit4:
                                digit3.requestFocus();
                                break;
                            case R.id.code_digit3:
                                digit2.requestFocus();
                                break;
                            case R.id.code_digit2:
                                digit1.requestFocus();
                                break;
                            case R.id.code_digit1:
                                break;
                        }
                    }
                    return;
                }
                switch (id) {
                    case R.id.code_digit1:
                        digit2.requestFocus();
                        break;
                    case R.id.code_digit2:
                        digit3.requestFocus();
                        break;
                    case R.id.code_digit3:
                        digit4.requestFocus();
                        break;
                    case R.id.code_digit4:
                        digit5.requestFocus();
                        break;
                    case R.id.code_digit5:
                        digit6.requestFocus();
                        break;
                    case R.id.code_digit6:
                        verifyCode();
                        break;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void verifyCode() {
        if(digit1.getText() == null || digit1.getText().toString() == ""){
            displayMissingDigit();
        }
        else if(digit2.getText() == null || digit2.getText().toString() == ""){
            displayMissingDigit();
        }
        else if(digit3.getText() == null || digit3.getText().toString() == ""){
            displayMissingDigit();
        }
        else if(digit4.getText() == null || digit4.getText().toString() == ""){
            displayMissingDigit();
        }
        else if(digit5.getText() == null || digit5.getText().toString() == ""){
            displayMissingDigit();
        }
        else if(digit6.getText() == null || digit6.getText().toString() == ""){
            displayMissingDigit();
        }else{
            String enteredCode = "";
            enteredCode += digit1.getText().toString();
            enteredCode += digit2.getText().toString();
            enteredCode += digit3.getText().toString();
            enteredCode += digit4.getText().toString();
            enteredCode += digit5.getText().toString();
            enteredCode += digit6.getText().toString();
            if(enteredCode.equals(verificationCode)){
                display_editpassword();
            }else{
                Toast.makeText(this, "Invalid Code. Please re-enter the verification code.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void displayMissingDigit(){
        Toast.makeText(this, "Please enter all digits of the verficiation code", Toast.LENGTH_SHORT).show();
    }

    private boolean validateEmail(String email) {
        EmailValidationResult.ValidationError error = EmailValidator.validateSyntax(email);
        if(error == null) {
            this.email = email;
            return true;
        }
        switch (error){
            case BLANK_ADDRESS:
                Toast.makeText(this, "No email specified", Toast.LENGTH_SHORT).show();
                break;
            case INVALID_SYNTAX:
                Toast.makeText(this, "Email is invalid", Toast.LENGTH_SHORT).show();
                break;
            case INVALID_USERNAME:
                Toast.makeText(this, "Username is invalid", Toast.LENGTH_SHORT).show();
                break;
            case INVALID_DOMAIN:
                Toast.makeText(this, "Email domain is invalid", Toast.LENGTH_SHORT).show();
                break;
            case INVALID_TLD:
                Toast.makeText(this, "Top level domain is invalid", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return false;
    }

    private boolean validatePhone(String phone) {
        if(phone.length() != 10) {
            Toast.makeText(this, "Phone is invalid", Toast.LENGTH_SHORT).show();
            return false;
        }
        for(int i=0; i<phone.length(); i++){
            if(!Character.isDigit(phone.charAt(i))){
                Toast.makeText(this, "Phone is invalid", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        this.phone = phone;
        return true;
    }

    private boolean validatePassword(){
        if(newPassword1EditText.getText() != null){
            newPassword1 = newPassword1EditText.getText().toString().trim();
            if(newPassword1.equalsIgnoreCase("")){
                Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(newPassword2EditText.getText() != null){
            newPassword2 = newPassword2EditText.getText().toString().trim();
            if(newPassword2.equalsIgnoreCase("")){
                Toast.makeText(this, "Please confirm password", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            Toast.makeText(this, "Please confirm password", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(!newPassword1.equals(newPassword2)){
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void requestVerificationCode(View view){
        if(!validateEmail(registeredEmail.getText().toString()) || !validatePhone(registeredPhone.getText().toString())){
            return;
        }
        try{
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String script = "request_verification_code.php";
            JSONObject params = new JSONObject();
            params.put("email", email);
            params.put("phone", phone);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    Globals.SERVER_URL + "/" + script,
                    params,
                    createListener_requestVerificationCode(),
                    createErrorListener_requestVerificationCode());

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    Globals.MY_SOCKET_TIMEOUT_MS,
                    0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            //progressTxtView.setText("Processing ...");
            requestQueue.add(jsonObjectRequest);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private Response.Listener<JSONObject> createListener_requestVerificationCode(){
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //progressTxtView.setText("Processing ... Done !");
                try{
                    int success = response.getInt("success");
                    if(success == 1) {
                        verificationCode = response.getString("code");
                        saveVerificationCodeFor30Minutes();
                        displayCodeVerification();
                    }else{
                        Toast.makeText(ResetPasswordActivity.this, response.getString("error_message"), Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(ResetPasswordActivity.this, "Exception occurred." + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private void saveVerificationCodeFor30Minutes() {
        Date date = new Date();
        long millis = date.getTime();
        prefs.edit().putString(Globals.PASS_RESET_VERIFICATION_CODE, verificationCode).commit();
        prefs.edit().putString(Globals.PASS_RESET_VERIFICATION_EMAIL, email).commit();
        prefs.edit().putLong(Globals.PASS_RESET_VERIFICATION_CODE_SAVE_TIME, date.getTime()).commit();
    }

    private void displayCodeVerification() {
        codeSendLayout.setVisibility(View.GONE);
        codeVerificationLayout.setVisibility(View.VISIBLE);
    }

    private void display_editpassword() {
        codeVerificationLayout.setVisibility(View.GONE);
        editPasswordLayout.setVisibility(View.VISIBLE);
        if(updatePasswordBtn == null){
            updatePasswordBtn = findViewById(R.id.newpassword_submit);
            updatePasswordBtn.setOnClickListener(createListener_updatePasswordBtn());
        }
    }

    private View.OnClickListener createListener_updatePasswordBtn() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validatePassword()){
                    return;
                }
                updatePasswordOnServer();
            }
        };
    }

    private Response.ErrorListener createErrorListener_requestVerificationCode(){
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressTxtView.setText("Processing ... Error!");
                Toast.makeText(ResetPasswordActivity.this, "Error response from server - " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void updatePasswordOnServer(){
        try{
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String script = "change_password.php";
            JSONObject params = new JSONObject();
            params.put("email", prefs.getString(Globals.PASS_RESET_VERIFICATION_EMAIL, ""));
            params.put("password", newPassword1);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    Globals.SERVER_URL + "/" + script,
                    params,
                    createListener_updatePasswordOnServer(),
                    createErrorListener_updatePasswordOnServer());

            //progressTxtView.setText("Processing ...");
            requestQueue.add(jsonObjectRequest);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private Response.Listener<JSONObject> createListener_updatePasswordOnServer(){
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //progressTxtView.setText("Processing ... Done !");
                try{
                    int success = response.getInt("success");
                    if(success == 1) {
                        clearPasswordResetDate();
                        Toast.makeText(ResetPasswordActivity.this, "Password updated successfully.", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(ResetPasswordActivity.this, response.getString("error_message"), Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(ResetPasswordActivity.this, "Exception occurred." + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private Response.ErrorListener createErrorListener_updatePasswordOnServer(){
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressTxtView.setText("Processing ... Error!");
                Toast.makeText(ResetPasswordActivity.this, "Error response from server - " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void alreadyHaveCodeClicked(View view) {
        if(!prefs.contains(Globals.PASS_RESET_VERIFICATION_CODE)){
            Toast.makeText(ResetPasswordActivity.this, "No code found.", Toast.LENGTH_SHORT).show();
            return;
        }
        Date saveTime = new Date(prefs.getLong(Globals.PASS_RESET_VERIFICATION_CODE_SAVE_TIME, 0));
        Date currentTime = new Date();
        long elapsedMinutes = TimeWrapper.difference(saveTime, currentTime, 2);
        if(elapsedMinutes > 10){
            clearPasswordResetDate();
            Toast.makeText(ResetPasswordActivity.this, "Code expired.", Toast.LENGTH_SHORT).show();
            return;
        }
        verificationCode = prefs.getString(Globals.PASS_RESET_VERIFICATION_CODE, null);
        if(verificationCode == null) {
            Toast.makeText(ResetPasswordActivity.this, "Code does not exist.", Toast.LENGTH_SHORT).show();
            return;
        }
        displayCodeVerification();
    }

    private void clearPasswordResetDate() {
        prefs.edit().remove(Globals.PASS_RESET_VERIFICATION_CODE_SAVE_TIME)
                .remove(Globals.PASS_RESET_VERIFICATION_CODE)
                .remove(Globals.PASS_RESET_VERIFICATION_EMAIL)
                .commit();
    }
}

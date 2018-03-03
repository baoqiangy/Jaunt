package csc445.missouriwestern.edu.jaunt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.concurrent.ThreadLocalRandom;

import csc445.missouriwestern.edu.jaunt.model.Driver;
import csc445.missouriwestern.edu.jaunt.utils.fonts.FontChangeCrawler;
import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private ImageView imageName;
    private Driver driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        displayAppNameImage();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "AvenirNextLTPro-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));
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
        if(authenticate()){
            Intent intent = new Intent(LoginActivity.this, AccountActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(LoginActivity.this, "Login failed.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean authenticate(){
        int randomNum = ThreadLocalRandom.current().nextInt(0, 2);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if(randomNum == 1) {
            driver = new Driver();
            driver.setFirstName("Baoqiang");
            driver.setLastName("Yan");
            driver.setEmail("byan@missouriwestern.edu");
            String email = "byan@missouriwestern.edu";
            Paper.book("driver_"+email).write(Globals.ACCOUNT_INFO_KEY, driver);
            prefs.edit().putBoolean("signed_in", true).putString("email", email).commit();
        }else{
            prefs.edit().remove("email").putBoolean("signed_in", false).commit();
        }
        return randomNum == 1;
    }

    public void forgotPasswordClicked(View view) {
        Toast.makeText(LoginActivity.this, "Forgot password clicked.", Toast.LENGTH_SHORT).show();
    }
}

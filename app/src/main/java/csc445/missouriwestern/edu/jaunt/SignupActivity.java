package csc445.missouriwestern.edu.jaunt;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ThreadLocalRandom;

import csc445.missouriwestern.edu.jaunt.model.Driver;

public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        TextView titleTextView = findViewById(R.id.register_title);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/AvenirNextCondensed-Regular.ttf");
        titleTextView.setTypeface(custom_font);

    }

    public void signUpButtonClicked(View view) {
        if(signUp()){
            Intent intent = new Intent(SignupActivity.this, AccountActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(SignupActivity.this, "Signup failed.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean signUp(){
        int randomNum = ThreadLocalRandom.current().nextInt(0, 2);
        if(randomNum == 1) {
            Driver driver = new Driver(1, "Baoqiang", "Yan", "byan@missouriwestern.edu");
        }
        return randomNum == 1;
    }
}

package csc445.missouriwestern.edu.jaunt;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import csc445.missouriwestern.edu.jaunt.utils.fonts.FontChangeCrawler;

/**
 * Created by byan on 2/17/2018.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "AvenirNextLTPro-Regular.ttf");
        fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));
    }
}

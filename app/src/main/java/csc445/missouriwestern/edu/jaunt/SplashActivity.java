package csc445.missouriwestern.edu.jaunt;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    private ImageView imageLogo;
    private ImageView imageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        displaySplashAnim();
    }

    private void displaySplashAnim() {
        imageLogo = findViewById(R.id.app_logo);
        imageName = findViewById(R.id.app_name);
        imageName.setVisibility(View.INVISIBLE);

        final ContextThemeWrapper wrapper = new ContextThemeWrapper(this, R.style.AppNameImageDefaultColor);
        final Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_jaunttitle, wrapper.getTheme());
        imageName.setImageDrawable(drawable);

        Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_splash_scale);
        final Animation alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_alpha_in);
        alphaAnimation.setDuration(2000);

        final Intent login = new Intent(this, LoginActivity.class);

        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageName.setVisibility(View.VISIBLE);
                imageName.startAnimation(alphaAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(login);
                overridePendingTransition(R.anim.anim_alpha_out, R.anim.anim_alpha_in);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageLogo.startAnimation(scaleAnimation);
    }
}

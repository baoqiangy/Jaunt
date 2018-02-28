package csc445.missouriwestern.edu.jaunt.extensions.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by byan on 2/18/2018.
 */

public class CustomToolbar extends Toolbar {
    private Context context;

    public CustomToolbar(Context context) {
        super(context);
        this.context = context;
        setFont();
    }

    public CustomToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setFont();
    }

    public CustomToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setFont();
    }

    private void setFont() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                if (tv.getText().equals(getTitle())) {
                    applyFont(tv, (Activity) this.context);
                    break;
                }
            }
        }
    }

    private void applyFont(TextView tv, Activity context) {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/AvenirNextCondensed-Heavy.ttf");
        tv.setTypeface(font);
    }
}

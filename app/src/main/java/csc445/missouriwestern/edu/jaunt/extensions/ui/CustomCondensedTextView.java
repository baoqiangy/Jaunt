package csc445.missouriwestern.edu.jaunt.extensions.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

;

/**
 * Created by byan on 2/17/2018.
 */

public class CustomCondensedTextView extends AppCompatTextView {
    public CustomCondensedTextView(Context context) {
        super(context);
        setFont();
    }
    public CustomCondensedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public CustomCondensedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/AvenirNextCondensed-Regular.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}

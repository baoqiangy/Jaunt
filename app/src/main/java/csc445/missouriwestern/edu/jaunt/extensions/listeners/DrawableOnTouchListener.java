package csc445.missouriwestern.edu.jaunt.extensions.listeners;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by byan on 2/23/2018.
 * Borrowed from https://stackoverflow.com/questions/3554377/handling-click-events-on-a-drawable-within-an-edittext
 */

public abstract class DrawableOnTouchListener implements View.OnTouchListener {
    Drawable drawable;
    private int fuzz = 10;
    private int position;
    Context context;

    public static enum Position {
        START, TOP, END, BOTTOM
    }

    /**
     * @param view
     */
    public DrawableOnTouchListener(Context context, TextView view, Position position) {
        super();
        final Drawable[] drawables = view.getCompoundDrawablesRelative();
        this.position = position.ordinal();
        this.context = context;
        if (drawables != null && drawables.length > this.position)
            this.drawable = drawables[this.position];
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
     */
    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && drawable != null) {
            float density =  context.getResources().getDisplayMetrics().density;

            final int touch_x_dp = (int) (event.getX() / density);
            final int touch_y_dp = (int) (event.getY() / density);
            final Rect bounds = drawable.getBounds();
            final int bounds_width_dp  = (int) (bounds.width() / density);
            final int bounds_height_dp = (int) (bounds.height() / density);

            int drawable_x_Dp = (int)(v.getX() / density);
            int drawable_y_Dp = (int)(v.getY() / density);
            int fuzz_dp = (int) (fuzz / density);
            
            if (touch_x_dp >= (drawable_x_Dp - fuzz_dp) && touch_x_dp <= (drawable_x_Dp + bounds_width_dp + fuzz_dp)
                    && touch_y_dp >= (drawable_y_Dp - fuzz_dp) && touch_y_dp <= (drawable_y_Dp + bounds_height_dp + fuzz_dp)) {
                return onDrawableTouch(event);
            }
        }
        return false;
    }

    public abstract boolean onDrawableTouch(final MotionEvent event);

}

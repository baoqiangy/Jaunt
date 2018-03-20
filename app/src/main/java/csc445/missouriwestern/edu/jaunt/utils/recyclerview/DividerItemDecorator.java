package csc445.missouriwestern.edu.jaunt.utils.recyclerview;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by byan on 3/20/2018.
 * https://stackoverflow.com/questions/46215810/recyclerview-remove-divider-decorator-after-the-last-item?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
 */

public class DividerItemDecorator extends RecyclerView.ItemDecoration {
    private Drawable mDivider;

    public DividerItemDecorator(Drawable divider) {
        mDivider = divider;
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        int dividerLeft = parent.getPaddingLeft();
        int dividerRight = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i <= childCount - 2; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int dividerTop = child.getBottom() + params.bottomMargin;
            int dividerBottom = dividerTop + mDivider.getIntrinsicHeight();

            mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
            mDivider.draw(canvas);
        }
    }
}
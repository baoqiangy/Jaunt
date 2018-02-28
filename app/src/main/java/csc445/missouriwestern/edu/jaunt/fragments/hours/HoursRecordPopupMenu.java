package csc445.missouriwestern.edu.jaunt.fragments.hours;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.view.View;

/**
 * Created by byan on 2/19/2018.
 */

public class HoursRecordPopupMenu extends PopupMenu {

    public HoursRecordPopupMenu(@NonNull Context context, @NonNull View anchor) {
        super(context, anchor);
    }

    public HoursRecordPopupMenu(@NonNull Context context, @NonNull View anchor, int gravity) {
        super(context, anchor, gravity);
    }

    public HoursRecordPopupMenu(@NonNull Context context, @NonNull View anchor, int gravity, int popupStyleAttr, int popupStyleRes) {
        super(context, anchor, gravity, popupStyleAttr, popupStyleRes);
    }

    @Override
    public void setOnMenuItemClickListener(@Nullable OnMenuItemClickListener listener) {
        super.setOnMenuItemClickListener(listener);
    }
}

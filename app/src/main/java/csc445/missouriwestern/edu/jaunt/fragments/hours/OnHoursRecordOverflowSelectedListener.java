package csc445.missouriwestern.edu.jaunt.fragments.hours;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.concurrent.ThreadLocalRandom;

import csc445.missouriwestern.edu.jaunt.R;

/**
 * Created by byan on 2/19/2018.
 * Borrowed from http://keepsafe.github.io/2014/11/19/building-a-custom-overflow-menu.html
 */

public class OnHoursRecordOverflowSelectedListener implements View.OnClickListener{
    private HoursRecord mRecord;
    private Context mContext;
    private HoursRecyclerViewAdapter.HoursRecordHolder recordHolder;

    public OnHoursRecordOverflowSelectedListener(Context context, HoursRecord record, HoursRecyclerViewAdapter.HoursRecordHolder recordHolder) {
        mContext = context;
        mRecord = record;
        this.recordHolder = recordHolder;
    }

    @Override
    public void onClick(View v) {
        // This is an android.support.v7.widget.PopupMenu;
        HoursRecordPopupMenu popupMenu = new HoursRecordPopupMenu(mContext, v);
        popupMenu.inflate(R.menu.hours_record_popup_menu);
        recordHolder.onClick(v); //this v is the fab icon
        // Select which items to display based upon the record state
        int randomNum = ThreadLocalRandom.current().nextInt(0, 2);
        if (randomNum==0) {
            popupMenu.getMenu().removeItem(R.id.hours_record_edit);
        } else {
            popupMenu.getMenu().removeItem(R.id.hours_record_delete);
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.hours_record_edit:
                        Toast.makeText(mContext, "Edit Selected", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.hours_record_delete:
                        Toast.makeText(mContext, "Delete Selected", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });

        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                recordHolder.dehighlight();
            }
        });

        // Force icons to show
        Object menuHelper;
        Class[] argTypes;
        try {
            /*
            *   Important - don't use the child class here. Use PopupMenu here
            * */
            Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
            fMenuHelper.setAccessible(true);
            menuHelper = fMenuHelper.get(popupMenu);
            argTypes = new Class[] { boolean.class };
            menuHelper.getClass().getDeclaredMethod("setForceShowIcon", argTypes).invoke(menuHelper, true);
        } catch (Exception e) {
            // Possible exceptions are NoSuchMethodError and NoSuchFieldError
            //
            // In either case, an exception indicates something is wrong with the reflection code, or the
            // structure of the PopupMenu class or its dependencies has changed.
            //
            // These exceptions should never happen since we're shipping the AppCompat library in our own apk,
            // but in the case that they do, we simply can't force icons to display, so log the error and
            // show the menu normally.
            Log.w("OverflowSelectListener", "error forcing menu icons to show", e);
            popupMenu.show();
            return;
        }

        popupMenu.show();
    }
}

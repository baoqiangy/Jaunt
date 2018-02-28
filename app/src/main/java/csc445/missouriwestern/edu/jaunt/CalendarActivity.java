package csc445.missouriwestern.edu.jaunt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.CalendarView;

public class CalendarActivity extends AppCompatActivity {

    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                int unbiasedMonth = month + 1;
                String date =   (unbiasedMonth>=10 ? unbiasedMonth : "0" + unbiasedMonth) + "/" +
                        (dayOfMonth>=10 ? dayOfMonth : "0" + dayOfMonth) + "/" + year;
                Intent returnIntent = new Intent();
                returnIntent.putExtra("date",date);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
    }
}

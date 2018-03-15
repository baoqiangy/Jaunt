package csc445.missouriwestern.edu.jaunt;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.joda.time.LocalTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import csc445.missouriwestern.edu.jaunt.fragments.hours.HoursRecord;
import csc445.missouriwestern.edu.jaunt.fragments.hours.TimeRange;
import csc445.missouriwestern.edu.jaunt.utils.date.TimeWrapper;
import csc445.missouriwestern.edu.jaunt.utils.userinfo.PersistenceWrapper;
import csc445.missouriwestern.edu.jaunt.utils.userinfo.PreferencesWrapper;

public class HoursEditActivity extends AppCompatActivity {

    private String TAG = "TAG_HoursEditActivity";
    private Context context;
    private String dayStr;
    private RecyclerView timeRangeRecyclerView;
    private TimeRangeRecyclerViewAdapter timeRangeRecyclerViewAdapter;
    private HoursRecord hoursRecord;
    private List<TimeRange> timeRanges;
    private TimeRangeRecyclerViewAdapter.RecyclerViewItemOnClickedListener listener;
    private SharedPreferences prefs;
    private TimePicker timePicker;
    private TextView dayTextView;
    private TextView servedTimeTextView;
    private TimeRange servedTimeRange;
    private int servedTimeRangeSeqNum;
    private boolean servingStart;
    private boolean switchingTimeInputField;
    private ImageButton addButton;
    private String book_name;
    private LinearLayoutManager recyclerViewLayoutManager;
    private ViewTreeObserver.OnGlobalLayoutListener recyclerViewLayoutListener;

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hours_edit);
        wireUpWidgets();
        context = this;
        recyclerViewLayoutManager = new LinearLayoutManager(context);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        book_name = PersistenceWrapper.getBookName();
        populateCurrentTimeRanges();
    }

    private void populateCurrentTimeRanges() {
        hoursRecord = getIntent().getParcelableExtra(Globals.HOURS_RECORD_KEY);
        if(hoursRecord == null){
            return;
        }
        dayStr = hoursRecord.getDayStr();
        timeRanges = hoursRecord.getAvailability();
        updateRecyclerView();
    }

    private void updateRecyclerView(){
        dayTextView.setText(dayStr);
        timeRangeRecyclerViewAdapter = new TimeRangeRecyclerViewAdapter(context, timeRanges);
        createListenerIfNotExist();
        timeRangeRecyclerViewAdapter.setListener(listener);
        timeRangeRecyclerView.setAdapter(timeRangeRecyclerViewAdapter);
        timeRangeRecyclerView.setLayoutManager(recyclerViewLayoutManager);

        if(timeRanges == null) return;

        toggleAddButton();
    }

    private void createListenerIfNotExist(){
        listener = new TimeRangeRecyclerViewAdapter.RecyclerViewItemOnClickedListener() {
            @Override
            public void recyclerViewItemClicked(RecyclerView.ViewHolder viewHolder) {
                TimeRange selectedRange = ((TimeRangeItemViewHolder)viewHolder).timeRange;
            }

            @Override
            public void timeRangeClicked(int seqNum, TimeRange timeRange, TextView textView) {
                servedTimeTextView  = textView;
                servedTimeRange     = timeRange;
                switchingTimeInputField = true;
                if(textView.getId() == R.id.start_time){
                    servingStart = true;
                    if(timeRange.isStartChanged() || !timeRange.isNewRange()) {
                        timePicker.setHour(timeRange.getStart().getHourOfDay());
                        timePicker.setMinute(timeRange.getStart().getMinuteOfHour());
                    }
                }
                if(textView.getId() == R.id.end_time){
                    servingStart = false;
                    if(timeRange.isEndChanged() || !timeRange.isNewRange()) {
                        timePicker.setHour(timeRange.getEnd().getHourOfDay());
                        timePicker.setMinute(timeRange.getEnd().getMinuteOfHour());
                    }
                }
                switchingTimeInputField = false;
                timeRangeRecyclerViewAdapter.setServingStart(servingStart);
                timeRangeRecyclerViewAdapter.setSelected_position(seqNum);
                timeRangeRecyclerViewAdapter.notifyDataSetChanged();
                Toast.makeText(HoursEditActivity.this, "timeRangeClicked", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void wireUpWidgets() {
        timePicker = findViewById(R.id.timePicker);
        timePicker.setOnTimeChangedListener(createTimePickerListener());
        dayTextView = findViewById(R.id.weekday);
        timeRangeRecyclerView = findViewById(R.id.hoursRecyclerView);
        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(createOnClickListen_addButton());

        simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
                int swipedSeqNum = ((TimeRangeItemViewHolder)viewHolder).getSeqNum();
                timeRanges.remove(swipedSeqNum);
                //if(timeRangeRecyclerViewAdapter.getSelected_position() > swipedSeqNum){
                //    timeRangeRecyclerViewAdapter.setSelected_position(timeRangeRecyclerViewAdapter.getSelected_position()-1);
                //}else if(timeRangeRecyclerViewAdapter.getSelected_position() == swipedSeqNum){
                    timeRangeRecyclerViewAdapter.setSelected_position(-1);
                    timeRangeRecyclerViewAdapter.setServingStart(true);
                //}
                timeRangeRecyclerViewAdapter.notifyDataSetChanged();
                servedTimeRange = null;
                servedTimeRangeSeqNum = -1;
                servedTimeTextView = null;
                servingStart = true;

                if(timeRanges != null){
                    toggleAddButton();
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(timeRangeRecyclerView);

        recyclerViewLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                updateServedTimeTextViewForTimePicker();
                timeRangeRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        };
    }

    private TimePicker.OnTimeChangedListener createTimePickerListener() {
        return new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                if(switchingTimeInputField || servedTimeTextView==null) return;
                LocalTime pickedTime = new LocalTime(hourOfDay, minute);
                servedTimeTextView.setText(TimeWrapper.jodaTimeToStr(pickedTime));
                if(servingStart){
                    servedTimeRange.setStartChanged(true);
                    servedTimeRange.setStart(pickedTime);
                }else{
                    servedTimeRange.setEndChanged(true);
                    servedTimeRange.setEnd(pickedTime);
                }
            }
        };
    }

    private View.OnClickListener createOnClickListen_addButton(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeRangeRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(recyclerViewLayoutListener);

                LocalTime start = new LocalTime();
                LocalTime end = new LocalTime();
                TimeRange timeRange = new TimeRange(start, end);
                timeRange.setNewRange(true);
                if(timeRanges == null){
                    timeRanges = new ArrayList<>();
                }
                timeRanges.add(timeRange);
                //clear the highlight
                timeRangeRecyclerViewAdapter.setSelected_position(timeRanges.size()-1);
                //this refreshes the recycler view
                timeRangeRecyclerViewAdapter.setTimeRanges(timeRanges);
                servedTimeRange = timeRange;
                servedTimeRangeSeqNum = timeRanges.size()-1;
                servingStart = true;
                timeRangeRecyclerViewAdapter.setServingStart(true);
                //updateServedTimeTextViewForTimePicker(); //layout not done yet
                toggleAddButton();
            }
        };
    }

    private void toggleAddButton(){
        if(timeRanges.size() == 4) {
            addButton.setVisibility(View.INVISIBLE);
        }else{
            addButton.setVisibility(View.VISIBLE);
        }
    }

    private void updateServedTimeTextViewForTimePicker(){
        if(timeRangeRecyclerView != null) {
            if(timeRangeRecyclerView.findViewHolderForAdapterPosition(servedTimeRangeSeqNum) != null ){
                servedTimeTextView = ((TimeRangeItemViewHolder)timeRangeRecyclerView.findViewHolderForAdapterPosition(servedTimeRangeSeqNum)).getStartTimeTextView();
            }
        }
    }

    public void saveClicked(View view) {
        if(timeRanges != null) {
            for(int i=0; i<timeRanges.size(); i++){
                timeRanges.get(i).setStartChanged(false);
                timeRanges.get(i).setEndChanged(false);
                timeRanges.get(i).setNewRange(false);
            }
        }
        hoursRecord.setAvailability(timeRanges);
        List<HoursRecord> hoursRecords = PersistenceWrapper.loadHoursRecords();
        for(int i=0; i<hoursRecords.size(); i++){
            if(hoursRecords.get(i).getDay() == hoursRecord.getDay()){
                hoursRecords.get(i).setAvailability(hoursRecord.getAvailability());
                break;
            }
        }

        PersistenceWrapper.saveHoursRecords(hoursRecords);
        updateHoursOnServer();
        finish();
    }

    private void updateHoursOnServer(){
        try{
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String script = "update_hours.php";
            JSONObject params = new JSONObject();
            params.put("driver_id", PreferencesWrapper.getDriverId());
            params.put("hours", PersistenceWrapper.loadHoursAsJsonList());

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    Globals.SERVER_URL + "/" + script,
                    params,
                    createListener_updateHoursOnServer(),
                    createErrorListener_updateHoursOnServer());

            //progressTxtView.setText("Processing ...");
            requestQueue.add(jsonObjectRequest);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private Response.Listener<JSONObject> createListener_updateHoursOnServer(){
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //progressTxtView.setText("Processing ... Done !");
                try{
                    int success = response.getInt("success");
                    if(success == 1) {
                        Toast.makeText(HoursEditActivity.this, "Hours Saved Successful!", Toast.LENGTH_SHORT).show();
                    }else{
                        Log.d(TAG, response.getString("error_message"));
                        Toast.makeText(HoursEditActivity.this, response.getString("error_message"), Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(HoursEditActivity.this, "Exception occurred." + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private Response.ErrorListener createErrorListener_updateHoursOnServer(){
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressTxtView.setText("Processing ... Error!");
                Log.d(TAG, error.getMessage());
                Toast.makeText(HoursEditActivity.this, "Error response from server - " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }
}

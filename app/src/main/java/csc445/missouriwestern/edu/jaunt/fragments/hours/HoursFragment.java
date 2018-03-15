package csc445.missouriwestern.edu.jaunt.fragments.hours;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import csc445.missouriwestern.edu.jaunt.Globals;
import csc445.missouriwestern.edu.jaunt.Jaunt;
import csc445.missouriwestern.edu.jaunt.R;
import csc445.missouriwestern.edu.jaunt.utils.userinfo.PersistenceWrapper;
import csc445.missouriwestern.edu.jaunt.utils.userinfo.PreferencesWrapper;

;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HoursFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HoursFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView mRecyclerView;
    private HoursRecyclerViewAdapter recyclerViewAdapter;
    private List<HoursRecord> hoursRecords;
    private String book_name;
    private int driverId;
    private String TAG = "TAG_HoursFragment";

    public HoursFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HoursFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HoursFragment newInstance(String param1, String param2) {
        HoursFragment fragment = new HoursFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        populateCurrentHours();
    }

    private void populateCurrentHours() {
        if(PreferencesWrapper.isSignedIn()) {
            driverId = PreferencesWrapper.getDriverId();
            book_name = PreferencesWrapper.getDriverEmail();
        }

        hoursRecords = PersistenceWrapper.loadHoursRecords();
        if(hoursRecords == null) {
            fetchHoursRecords();
        }else{
            updateRecyclerView();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hours, container, false);
        mRecyclerView = view.findViewById(R.id.hours_recyclerView);
        return view;
    }

    private void updateRecyclerView(){
        recyclerViewAdapter = new HoursRecyclerViewAdapter(getContext(), hoursRecords);
        mRecyclerView.setAdapter(recyclerViewAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),
                DividerItemDecoration.VERTICAL));
    }
    private void dimOtherRows(){
//        // Dim out all the other list items if they exist
//        int firstPos = mRecyclerView.f - mRecyclerView.getHeaderViewsCount();
//        final int ourPos = mListAdapter.getPosition(mAlbum) - firstPos;
//        int count = mRecyclerView.getChildCount();
//        for (int i = 0; i <= count; i++) {
//            if (i == ourPos) {
//                continue;
//            }
//
//            final View child = mRecyclerView.getChildAt(i);
//            if (child != null) {
//                child.clearAnimation();
//                child.startAnimation(mFadeOut);
//            }
//        }
//
//        // Make sure to bring them back to normal after the menu is gone
//        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
//            @Override
//            public void onDismiss(PopupMenu popupMenu) {
//                int count = mRecyclerView.getChildCount();
//                for (int i = 0; i <= count; i++) {
//                    if (i == ourPos) {
//                        continue;
//                    }
//
//                    final View v = mRecyclerView.getChildAt(i);
//                    if (v != null) {
//                        v.clearAnimation();
//                        v.startAnimation(mFadeIn);
//                    }
//                }
//            }
//        });
    }

//    private void fetchHoursRecords() {
//        hoursRecords = new ArrayList<>();
//        List<TimeRange> ranges = new ArrayList<>();
//        ranges.add(new TimeRange(new LocalTime(10, 30), new LocalTime(12, 30)));
//        //ranges.add(new TimeRange(new LocalTime(14, 30), new LocalTime(19, 30)));
//        hoursRecords.add(new HoursRecord(0, ranges));
//        hoursRecords.add(new HoursRecord(1, ranges));
//        hoursRecords.add(new HoursRecord(2, ranges));
//        hoursRecords.add(new HoursRecord(3, ranges));
//        hoursRecords.add(new HoursRecord(4, ranges));
//        hoursRecords.add(new HoursRecord(5, ranges));
//        hoursRecords.add(new HoursRecord(6, ranges));
//        HoursRecord hoursRecord;
//        for(int i=0; i<hoursRecords.size(); i++) {
//            hoursRecord = hoursRecords.get(i);
//        }
//        updateRecyclerView();
//    }

    private void fetchHoursRecords(){
        try{
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            String script = "list_driver_hours.php";
            JSONObject params = new JSONObject();
            params.put("driver_id", String.valueOf(driverId));

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    Globals.SERVER_URL + "/" + script,
                    params,
                    createListener_fetchHoursRecords(),
                    createErrorListener_fetchHoursRecords());

            //progressTxtView.setText("Processing ...");
            requestQueue.add(jsonObjectRequest);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private Response.Listener<JSONObject> createListener_fetchHoursRecords(){
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //progressTxtView.setText("Processing ... Done !");
                try{
                    int success = response.getInt("success");
                    if(success == 1) {

                        JSONArray jsonObjects = response.getJSONArray("hoursRecords");

                        hoursRecords = initiateHoursRecords();

                        JSONObject jsonObject = null;
                        for (int i=0; i<jsonObjects.length(); i++) {
                            jsonObject = (JSONObject)jsonObjects.get(i);
                            HoursRecord r = new HoursRecord(jsonObject);
                            for(int j=0; j<hoursRecords.size(); j++){
                                if(jsonObject.getInt("day") == hoursRecords.get(j).getDay()){
                                    hoursRecords.get(j).setAvailability(r.getAvailability());
                                    break;
                                }
                            }
                        }

                        updateRecyclerView();

                        PersistenceWrapper.saveHoursRecords(hoursRecords);
                    }else{
                        Log.d(TAG, response.getString("error_message"));
                        Toast.makeText(Jaunt.getAppContext(), response.getString("error_message"), Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(Jaunt.getAppContext(), "Exception occurred." + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    private List<HoursRecord> initiateHoursRecords() {
        List<HoursRecord> records = new ArrayList<HoursRecord>();
        records.add(new HoursRecord(0));
        records.add(new HoursRecord(1));
        records.add(new HoursRecord(2));
        records.add(new HoursRecord(3));
        records.add(new HoursRecord(4));
        records.add(new HoursRecord(5));
        records.add(new HoursRecord(6));
        return records;
    }

    private Response.ErrorListener createErrorListener_fetchHoursRecords(){
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressTxtView.setText("Processing ... Error!");
                Log.d(TAG, error.getMessage());
                Toast.makeText(Jaunt.getAppContext(), "Error response from server - " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        };
    }

}

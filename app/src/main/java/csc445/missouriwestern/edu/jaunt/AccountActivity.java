package csc445.missouriwestern.edu.jaunt;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.IpCons;
import com.esafirm.imagepicker.model.Image;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import csc445.missouriwestern.edu.jaunt.extensions.ui.CustomTextView;
import csc445.missouriwestern.edu.jaunt.fragments.AccountViewPagerAdapter;
import csc445.missouriwestern.edu.jaunt.fragments.earnings.EarningsFragment;
import csc445.missouriwestern.edu.jaunt.fragments.history.HistoryFragment;
import csc445.missouriwestern.edu.jaunt.fragments.hours.HoursFragment;
import csc445.missouriwestern.edu.jaunt.model.Driver;
import csc445.missouriwestern.edu.jaunt.thirdparty.imagepicker.ImagePickerWrapper;
import csc445.missouriwestern.edu.jaunt.thirdparty.imageuploader.AppHelper;
import csc445.missouriwestern.edu.jaunt.thirdparty.imageuploader.VolleyMultipartRequest;
import csc445.missouriwestern.edu.jaunt.thirdparty.imageuploader.VolleySingleton;
import io.paperdb.Paper;

public class AccountActivity extends BaseActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AccountViewPagerAdapter viewPagerAdapter;
    private ImageView profileImageView;
    private CustomTextView profileNameTextView;
    private CustomTextView profileLocationTextView;
    private Driver me;
    private MenuItem actMore;
    private String selectedProfilePhotoPath;
    private RequestOptions signatureOptions;
    private boolean uploadingProfilePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        setupOptionsMenu();
        setupBackTitle();
        profileImageView = findViewById(R.id.profile_imageview);
        profileImageView.setClipToOutline(true);
        profileNameTextView = findViewById(R.id.profile_name);
        profileLocationTextView = findViewById(R.id.profile_location);
        populateFragments();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean signed_in = prefs.getBoolean("signed_in", false);
        if(signed_in){
            String email = prefs.getString("email", null);
            String book_name = "driver_"+email;
            me = Paper.book(book_name).read(Globals.ACCOUNT_INFO_KEY);
            if(me != null){
                profileNameTextView.setText(me.getFirstName() +" " + me.getLastName());
                Address myaddress = me.getAddress();
                if(myaddress != null){
                    profileLocationTextView.setText(myaddress.getLocality() +", "+myaddress.getAdminArea());
                }else{
                    profileLocationTextView.setText("");
                }

                if(uploadingProfilePhoto){
                    return;
                }
                if(me.isHasProfilePhoto()){
                    String image_url = Globals.SERVER_URL + "/images/driver/profile_" + me.getDriverId() + ".jpg";
                    if(signatureOptions == null){
                        signatureOptions = new RequestOptions().signature(new ObjectKey(System.currentTimeMillis()));
                    }
                    Glide.with(this).
                            load(image_url).
                            apply(signatureOptions).
                            into(profileImageView);
                }
            }
        }
    }

    private void setupOptionsMenu() {
        Toolbar toolbar = findViewById(R.id.account_toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.account_options_menu, menu);
        actMore = menu.getItem(0);
        return super.onCreateOptionsMenu(menu);
    }


    private void setupBackTitle(){
        //back arrow
        final Toolbar toolbar = findViewById(R.id.account_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //hide title when expanded and display it when collapsed
        //borrowed from https://stackoverflow.com/questions/31662416/show-collapsingtoolbarlayout-title-only-when-collapsed
        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.account_collaspe_layout);
        collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(this, R.color.white));
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.account_appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setCollapsedTitleGravity(Gravity.CENTER);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    if(actMore != null) {
                        actMore.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
                    }
                    collapsingToolbarLayout.setTitle("Account");
                    isShow = true;
                } else if(isShow) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    if(actMore != null) {
                        actMore.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                    }
                    collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });
    }

    private void populateFragments(){
        tabLayout = findViewById(R.id.account_tablayout_id);
        viewPager = findViewById(R.id.account_viewpager_id);
        viewPagerAdapter = new AccountViewPagerAdapter(getSupportFragmentManager());
        if(viewPagerAdapter != null) {
            viewPagerAdapter.addFragment(new HoursFragment(), "MY HOURS", null);
            viewPagerAdapter.addFragment(new HistoryFragment(), "HISTORY", null);
            viewPagerAdapter.addFragment(new EarningsFragment(), "EARNINGS", null);
            viewPager.setAdapter(viewPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
            for(int i=0; i<tabLayout.getTabCount(); i++) {
                Integer r = viewPagerAdapter.getTabIcon(i);
                if(r != null){
                    tabLayout.getTabAt(i).setIcon(r);
                }
            }
            tabLayout.getTabAt(2).select();
        }
    }

    public void photo_clicked(View view) {
        Toast.makeText(AccountActivity.this, "Display UI for photo upload...", Toast.LENGTH_SHORT).show();
        ImagePicker imagePicker = ImagePicker.create(this);
        ImagePickerWrapper.pickImage(imagePicker);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == Activity.RESULT_OK){
            if (ImagePicker.shouldHandle(IpCons.RC_IMAGE_PICKER, resultCode, data)) {
                // Get a list of picked images
                List<Image> images = ImagePicker.getImages(data);
                // or get a single image only
                Image image = ImagePicker.getFirstImageOrNull(data);
                selectedProfilePhotoPath = image.getPath();
                if(image != null) {
                    profileImageView.setImageBitmap(BitmapFactory.decodeFile(selectedProfilePhotoPath));
                    uploadingProfilePhoto = true;
                    uploadProfilePhotoIfDifferent();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

//        List<Image> images = ImagePicker.getImages(data);
//        if (images != null && !images.isEmpty()) {
//            profileImageView.setImageBitmap(BitmapFactory.decodeFile(images.get(0).getPath()));
//        }
//        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.account_logout:
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AccountActivity.this);
                prefs.edit().putBoolean("signed_in", false).remove("email").commit();
                finish();
                return true;
            case R.id.account_profile:
                //Toast.makeText(AccountActivity.this, "Display UI for profile...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AccountActivity.this, ProfileActivity.class);
                startActivity(intent);
                return true;
            case R.id.account_settings:
                //Toast.makeText(AccountActivity.this, "Display UI for settings...", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.driver_register:
                //Toast.makeText(AccountActivity.this, "Display UI for driver registration...", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //Toast.makeText(this, "onBackPressed called", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

    private void uploadProfilePhotoIfDifferent() {
        if(selectedProfilePhotoPath == null || selectedProfilePhotoPath.trim().length() == 0) {
            return;
        }
        String url = Globals.SERVER_URL + "/upload_image.php";

        VolleyMultipartRequest uploadRequest = new VolleyMultipartRequest(Request.Method.POST,
                url, createListener_uploadProfilePhoto(), createErrorListener_updateProfilePhoto()){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("driver_id", String.valueOf(me.getDriverId()));
                params.put("purpose", "profile");
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                params.put("profile", new DataPart("profile_"+me.getDriverId()+".jpg",
                        AppHelper.getBytesFromImagePath(getBaseContext(), selectedProfilePhotoPath), "image/jpeg"));
                //params.put("cover", new DataPart("file_cover.jpg", AppHelper.getFileDataFromDrawable(getBaseContext(), mCoverImage.getDrawable()), "image/jpeg"));

                return params;
            }
        };
        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(uploadRequest);
    }

    private void updateProfilePhotoWhenUploadSucceeded(){
        me.setHasProfilePhoto(true);
        Paper.book("driver_" + me.getEmail()).write(Globals.ACCOUNT_INFO_KEY, me);

        String image_url = Globals.SERVER_URL + "/images/driver/profile_" + me.getDriverId() + ".jpg";
        signatureOptions = new RequestOptions().signature(new ObjectKey(System.currentTimeMillis()));
        Glide.with(this).
                load(image_url).
                apply(signatureOptions);
                //into(profileImageView);
    }

    private Response.ErrorListener createErrorListener_updateProfilePhoto() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                uploadingProfilePhoto = false;
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");

                        Log.e("Error Status", status);
                        Log.e("Error Message", message);

                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message+" Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message+ " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message+" Something is getting wrong";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("Error", errorMessage);
                Toast.makeText(AccountActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        };
    }

    private Response.Listener<NetworkResponse> createListener_uploadProfilePhoto() {
        return new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                uploadingProfilePhoto = false;
                String resultResponse = new String(response.data);
                try {
                    JSONObject result = new JSONObject(resultResponse);
                    int status = result.getInt("success");
                    String errorMessage = result.getString("error_message");

                    if (status == 1) {
                        // tell everybody you have succed upload image and post strings
                        Toast.makeText(AccountActivity.this, "Profile photo uploaded successfully.", Toast.LENGTH_SHORT).show();
                        updateProfilePhotoWhenUploadSucceeded();
                    } else {
                        Toast.makeText(AccountActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

}

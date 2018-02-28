package csc445.missouriwestern.edu.jaunt;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import csc445.missouriwestern.edu.jaunt.fragments.AccountViewPagerAdapter;
import csc445.missouriwestern.edu.jaunt.fragments.earnings.EarningsFragment;
import csc445.missouriwestern.edu.jaunt.fragments.history.HistoryFragment;
import csc445.missouriwestern.edu.jaunt.fragments.hours.HoursFragment;

public class AccountActivity extends BaseActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AccountViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        setupBackTitle();
        setupOptionsMenu();
        findViewById(R.id.profile_imageview).setClipToOutline(true);
        populateFragments();
    }

    private void setupOptionsMenu() {
        Toolbar toolbar = findViewById(R.id.account_toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.account_options_menu, menu);
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
                collapsingToolbarLayout.setCollapsedTitleGravity(Gravity.CENTER);
                if (scrollRange + verticalOffset == 0) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    collapsingToolbarLayout.setTitle("My Account");
                    isShow = true;
                } else if(isShow) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.account_logout:
                onBackPressed();
                return true;
            case R.id.account_profile:
                Toast.makeText(AccountActivity.this, "Display UI for profile...", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.account_settings:
                Toast.makeText(AccountActivity.this, "Display UI for settings...", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.driver_register:
                Toast.makeText(AccountActivity.this, "Display UI for driver registration...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AccountActivity.this, RegisterDriveActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "onBackPressed called", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }
}

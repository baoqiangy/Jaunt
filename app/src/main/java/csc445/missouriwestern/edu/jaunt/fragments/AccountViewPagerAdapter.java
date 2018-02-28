package csc445.missouriwestern.edu.jaunt.fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by byan on 1/25/2018.
 */

public class AccountViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<Fragment>();
    private final List<String> titleList = new ArrayList<String>();
    private final List<Integer> iconList = new ArrayList<Integer>();

    public AccountViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    public Integer getTabIcon(int position){
        return iconList.get(position);
    }

    public void addFragment(Fragment fragment, String title, Integer iconId){
        fragmentList.add(fragment);
        titleList.add(title);
        iconList.add(iconId);
    }
}

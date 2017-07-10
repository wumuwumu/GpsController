package com.sciento.wumu.gpscontroller.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wumu on 17-5-15.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private  final List<Fragment> mfragmentlist = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        return mfragmentlist.get(position);
    }

    @Override
    public int getCount() {
        return mfragmentlist.size();
    }


    public void addFragment(Fragment fragment)
    {
        mfragmentlist.add(fragment);
    }
}

package com.sciento.wumu.gpscontroller.CommonModule;

import android.Manifest;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.sciento.wumu.gpscontroller.DeviceModule.DeviceFragment;
import com.sciento.wumu.gpscontroller.HomeModule.HomeFragment;
import com.sciento.wumu.gpscontroller.PersonModule.PersonFragment;
import com.sciento.wumu.gpscontroller.R;
import com.sciento.wumu.gpscontroller.View.NoScrollViewPager;
import com.sciento.wumu.gpscontroller.adapter.ViewPagerAdapter;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

public class MainActivity extends DeviceModuleBaseActivity {

   Context context = null;

    private BottomNavigationView bottomNavigationView;
    private NoScrollViewPager noScrollViewPager;

    //Fragment
    HomeFragment homeFragment = null;
    PersonFragment personFragment =null;
    DeviceFragment deviceFragment =null;
    MenuItem prevMenuItem =null;

    private static final String TAG = "MainActivity";
    private static final  int REQUEST_CODE_SETTING = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        init();
        initEvent();
        setupViewPager(noScrollViewPager);



    }

    private void initEvent() {
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId())
                        {
                            case  R.id.navigation_home:
                                noScrollViewPager.setCurrentItem(0);
                                return true;
                            case  R.id.navigation_device:
                                noScrollViewPager.setCurrentItem(1);
                                return true;
                            case  R.id.navigation_person:
                                noScrollViewPager.setCurrentItem(2);
                                return true;
                        }
                        return false;
                    }
                }
        );

        noScrollViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: "+position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void init() {
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        noScrollViewPager = (NoScrollViewPager)findViewById(R.id.viewpager);
    }


    private void setupViewPager(NoScrollViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        homeFragment = HomeFragment.newInstance();
        deviceFragment = DeviceFragment.newInstance();
        personFragment = PersonFragment.newInstance();
        viewPagerAdapter.addFragment(homeFragment);
        viewPagerAdapter.addFragment(deviceFragment);
        viewPagerAdapter.addFragment(personFragment);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(viewPagerAdapter);


    }



}

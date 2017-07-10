package com.sciento.wumu.gpscontroller.CommonModule;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sciento.wumu.gpscontroller.DeviceModule.DeviceFragment;
import com.sciento.wumu.gpscontroller.MqttModule.DeviceLocation;
import com.sciento.wumu.gpscontroller.Utils.NetworkUtils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

/**
 * Created by wumu on 17-7-7.
 */

class BaseActivity extends AppCompatActivity {

    private static final  int REQUEST_CODE_SETTING = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        AndPermission.with(this)
                .requestCode(REQUEST_CODE_SETTING)
                .permission(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.INTERNET,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CHANGE_WIFI_STATE,
                        Manifest.permission.CHANGE_CONFIGURATION,
                        Manifest.permission.WAKE_LOCK,
                        Manifest.permission.WRITE_SETTINGS,
                        Manifest.permission.WAKE_LOCK
                ).rationale(new RationaleListener() {

            @Override
            public void showRequestPermissionRationale(int arg0, Rationale arg1) {
                AndPermission.rationaleDialog(BaseActivity.this, arg1).show();
            }
        }).start();

        if(NetworkUtils.isConnected(BaseActivity.this))
        {
            DeviceLocation.getInstance().connect();
        }
    }


}
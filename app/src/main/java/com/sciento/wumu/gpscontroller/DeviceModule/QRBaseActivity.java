package com.sciento.wumu.gpscontroller.DeviceModule;

import android.Manifest;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

/**
 * Created by wumu on 2017/8/21.
 */

public class QRBaseActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SETTING = 201;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getpermission();
    }

    private void getpermission() {
        AndPermission.with(this)
                .requestCode(REQUEST_CODE_SETTING)
                .permission(
                        Manifest.permission.CAMERA,
                        Manifest.permission.VIBRATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).rationale(new RationaleListener() {

            @Override
            public void showRequestPermissionRationale(int arg0, Rationale arg1) {
                AndPermission.rationaleDialog(QRBaseActivity.this, arg1).show();
            }
        }).start();
    }
}

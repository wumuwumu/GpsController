package com.sciento.wumu.gpscontroller.DeviceModule;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sciento.wumu.gpscontroller.R;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;


public class QRcodeActivity extends AppCompatActivity implements QRCodeView.Delegate {
    private static final String TAG = QRcodeActivity.class.getSimpleName();
    private static final int REQUEST_CODE_SETTING = 200;
    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 1111;

    @BindView(R.id.qrcodeview)
    QRCodeView QRCodeView;
    @BindView(R.id.btn_qrcode_back)
    Button btnQrcodeBack;
    @BindView(R.id.btn_find_image)
    Button btnFindImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        ButterKnife.bind(this);
        getpermission();
        init();
    }

    private void init() {
        QRCodeView.setDelegate(this);

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
                AndPermission.rationaleDialog(QRcodeActivity.this, arg1).show();
            }
        }).start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        QRCodeView.startCamera();
        QRCodeView.showScanRect();
        QRCodeView.startSpot();
    }

    @Override
    protected void onStop() {
        QRCodeView.startCamera();
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        QRCodeView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        QRCodeView.startSpot();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        QRCodeView.showScanRect();

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
            final String picturePath = BGAPhotoPickerActivity.getSelectedImages(data).get(0);

            /*
            这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
            请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考 https://github.com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
             */
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    return QRCodeDecoder.syncDecodeQRCode(picturePath);
                }

                @Override
                protected void onPostExecute(String result) {
                    if (TextUtils.isEmpty(result)) {
                        Toast.makeText(QRcodeActivity.this, "未发现二维码", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(QRcodeActivity.this, result, Toast.LENGTH_SHORT).show();
                    }
                }
            }.execute();
        }
    }

    @OnClick(R.id.btn_find_image)
    void OnClick(View view){
        switch (view.getId())
        {
            case R.id.btn_find_image:
                startActivityForResult(BGAPhotoPickerActivity.newIntent(this, null, 1, null, false), REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
                break;
        }
    }
}

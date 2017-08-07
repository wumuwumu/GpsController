package com.sciento.wumu.gpscontroller.ControllerModule;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amap.api.maps.AMap;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.sciento.wumu.gpscontroller.DeviceSdk.DeviceController;
import com.sciento.wumu.gpscontroller.DeviceSdk.FenceListener;
import com.sciento.wumu.gpscontroller.Model.Fence;
import com.sciento.wumu.gpscontroller.Model.FenceInfo;
import com.sciento.wumu.gpscontroller.Model.SendFenceBean;
import com.sciento.wumu.gpscontroller.MqttModule.CurrentLocation;
import com.sciento.wumu.gpscontroller.R;
import com.sciento.wumu.gpscontroller.Utils.ProgressDialogUtils;
import com.sciento.wumu.gpscontroller.Utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FenceActivity extends AppCompatActivity {

    @BindView(R.id.fence_mapview)
    TextureMapView fenceMapView;
    @BindView(R.id.et_radis)
    EditText etRadis;
    @BindView(R.id.btn_send_fence)
    Button btnSendFence;

    private AMap mainAmap = null;
    private UiSettings mUiSettings;
    Circle circle = null;

    String deviceId;

    private boolean enopen =false;


    private final int MSG_GET_FENCE_STATE = 33;
    CurrentLocation currentLocation = null;
    Fence fence = null;

    Handler fenceHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_GET_FENCE_STATE:
                    DeviceController.getInstance().getfencestate(deviceId);
                    break;
            }
        }
    };

    FenceListener fenceListener = new FenceListener() {
        @Override
        public void didSendFence(int sataus) {
            switch (sataus){
                case -1:
                    ToastUtils.makeShortText(getString(R.string.str_set_fence_fail),FenceActivity.this);
                    break;
                case 0:
                    ToastUtils.makeShortText(getString(R.string.str_set_fence_fail),FenceActivity.this);
                    break;
                case 1:
                    ToastUtils.makeShortText(getString(R.string.str_set_fence_success),FenceActivity.this);
                    etRadis.setEnabled(false);
                    btnSendFence.setText(R.string.str_close);
            }

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fence);
        ButterKnife.bind(this);

        fenceMapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
        fenceHandler.sendEmptyMessage(MSG_GET_FENCE_STATE);
    }

    private void init() {
        deviceId = getIntent().getExtras().getString("deviceid");
        if (mainAmap == null) {
            mainAmap = fenceMapView.getMap();
            mUiSettings = mainAmap.getUiSettings();
        }
        mUiSettings.setScaleControlsEnabled(true);
        mUiSettings.setCompassEnabled(true);
        mUiSettings.setMyLocationButtonEnabled(true);
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(10000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        mainAmap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        mainAmap.setMyLocationEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe
    public void getFence(FenceInfo fenceinfo) {
        if (fenceinfo.getResult().getId().equals(deviceId)) {
            if (fenceinfo.getStatus() == 1) {
                fence = fenceinfo.getResult();
                if (fence.isState()) {
                    etRadis.setText(fence.getRadius()+"");
                    etRadis.setEnabled(false);
                    btnSendFence.setText(getString(R.string.str_open));
                    enopen = true;
                }else {
                    btnSendFence.setText(getString(R.string.str_close));
                    enopen = false;
                }
            }
        }

    }

    @Subscribe
    public void getDevice(CurrentLocation currentLocation){
        this.currentLocation = currentLocation;
    }

    @OnClick(R.id.btn_send_fence)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_send_fence:
                if(enopen){
                    ProgressDialogUtils.getInstance().show(FenceActivity.this,R.string.str_set_fence);
                    String radis = etRadis.getText().toString();
                    int mradis = 0;
                    try{
                        if(radis.matches("[0-9]*")) {
                             mradis = Integer.parseInt(radis);
                        }
                    }catch (NumberFormatException e){
                        e.printStackTrace();
                        ToastUtils.makeShortText(getString(R.string.str_is_not_num),FenceActivity.this);
                    }
                    LatLng fenceLaLng = null;
                    if(currentLocation != null){
                        fenceLaLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                    }else {
                        fenceLaLng = new LatLng(fence.getLatitude(),fence.getLongitude());
                    }
                    drawCircle(fenceLaLng,mradis);
                    SendFenceBean sendFence = new SendFenceBean();
                    sendFence.setId(deviceId);
                    sendFence.setLatitude(fenceLaLng.latitude);
                    sendFence.setLongitude(fenceLaLng.longitude);
                    sendFence.setRadius((double)mradis);
                    sendFence.setState(true);
                    DeviceController.getInstance().sendFenceInfo(sendFence,fenceListener);

                }else {

                }
                break;
        }
    }

    private void drawCircle(LatLng latlng,int raduis){
        if(circle == null){
        circle = mainAmap.addCircle(new CircleOptions().
                center(latlng).
                radius(1000).
                fillColor(Color.argb(50, 1, 1, 1)).
                strokeColor(Color.argb(50, 1, 1, 1)).
                strokeWidth(15));
        }else {
            circle.setCenter(latlng);
            circle.setRadius(raduis);
        }
    }
}

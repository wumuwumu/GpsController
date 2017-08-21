package com.sciento.wumu.gpscontroller.HomeModule;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.sciento.wumu.gpscontroller.DeviceModule.DeviceBaseFragment;
import com.sciento.wumu.gpscontroller.DeviceSdk.DeviceController;
import com.sciento.wumu.gpscontroller.DeviceSdk.FenceListener;
import com.sciento.wumu.gpscontroller.Event.FreshUi;
import com.sciento.wumu.gpscontroller.Event.UnbindDevice;
import com.sciento.wumu.gpscontroller.Model.DeviceConfig;
import com.sciento.wumu.gpscontroller.Model.DeviceState;
import com.sciento.wumu.gpscontroller.Model.Fence;
import com.sciento.wumu.gpscontroller.Model.FenceInfo;
import com.sciento.wumu.gpscontroller.Model.SendFenceBean;
import com.sciento.wumu.gpscontroller.MqttModule.CurrentLocation;
import com.sciento.wumu.gpscontroller.MqttModule.DeviceLocation;
import com.sciento.wumu.gpscontroller.MqttModule.LocationToJson;
import com.sciento.wumu.gpscontroller.R;
import com.sciento.wumu.gpscontroller.Utils.ProgressDialogUtils;
import com.sciento.wumu.gpscontroller.Utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class HomeFragment extends Fragment implements
        AMap.OnMyLocationChangeListener {


    public static HashMap<String, Circle> deviceCircle = new HashMap<>();
    private final int MSG_UPDATE_ALARM_BTN = 623;
    private final int MSG_GET_FENCE_STATE = 624;
    @BindView(R.id.main_mapview)
    TextureMapView mainMapview;
    Unbinder unbinder;
    @BindView(R.id.et_fence_value)
    EditText etFenceValue;
    @BindView(R.id.btn_control_fence)
    Button btnControlFence;
    @BindView(R.id.ll_device_controller)
    LinearLayout llDeviceController;
    @BindView(R.id.btn_control_alarm)
    Button btnControlAlarm;
    @BindView(R.id.btn_close_windows)
    Button btnCloseWindows;
    boolean enswitch = false;
    boolean enConfig = false;
    String deviceId = null;
    Fence fence = null;
    @BindView(R.id.btn_control_config)
    Button btnControlConfig;
    Handler homeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_ALARM_BTN:
                    for (int i = 0; i < DeviceBaseFragment.deviceslist.size(); i++) {
                        if (DeviceBaseFragment.deviceslist.get(i).getJsonDevice().getId().equals(deviceId)) {
                            enswitch = DeviceBaseFragment.deviceslist.get(i).getJsonDevice().isPower();
                            enConfig = DeviceBaseFragment.deviceslist.get(i).getJsonDevice().isConfig();
                            if (!enswitch) {
                                btnControlAlarm.setText(getString(R.string.str_close));

                            } else {
                                btnControlAlarm.setText(getString(R.string.str_open));
                            }
                            if (!enConfig) {
                                btnControlConfig.setText(getString(R.string.str_close));
                            } else {
                                btnControlConfig.setText(getString(R.string.str_open));
                            }
                        }
                    }
                    break;

                case MSG_GET_FENCE_STATE:
                    DeviceController.getInstance().getfencestate(deviceId);
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private AMap mainAmap = null;
    private UiSettings mUiSettings;
    private LocationSource.OnLocationChangedListener mLocationChangedListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private HashMap<String, Marker> deviceMap = new HashMap<>();
    private HashMap<String, CurrentLocation> deviceCurrentLocation = new HashMap<>();
    private boolean enopen = false;
    FenceListener fenceListener = new FenceListener() {
        @Override
        public void didSendFence(int sataus) {
            ProgressDialogUtils.getInstance().dismiss();
            switch (sataus) {
                case -1:
                    ToastUtils.makeShortText(getString(R.string.str_link_setver_fail), getActivity());
                    break;
                case 0:
                    ToastUtils.makeShortText(getString(R.string.str_set_fence_fail), getActivity());
                    break;
                case 1:
                    ToastUtils.makeShortText(getString(R.string.str_set_fence_success), getActivity());
                    etFenceValue.setEnabled(false);
                    enopen = true;
                    btnControlFence.setText(R.string.str_close);
            }
        }
    };
    FenceListener fenceCancelListener = new FenceListener() {
        @Override
        public void didSendFence(int sataus) {
            ProgressDialogUtils.getInstance().dismiss();
            switch (sataus) {
                case -1:
                    ToastUtils.makeShortText(getString(R.string.str_link_setver_fail), getActivity());
                    break;
                case 0:
                    ToastUtils.makeShortText(getString(R.string.str_cancel_fence_fail), getActivity());
                    break;
                case 1:
                    ToastUtils.makeShortText(getString(R.string.str_cancel_fence_success), getActivity());
                    etFenceValue.setEnabled(true);
                    enopen = false;
                    btnControlFence.setText(R.string.str_open);
            }

        }
    };

    private View view;

    public HomeFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        mainMapview.onCreate(savedInstanceState);// 此方法必须重写
        init();
        initEvent();
        return view;
    }

    private void init() {
        if (mainAmap == null) {
            mainAmap = mainMapview.getMap();
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
        mainAmap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        mainAmap.setOnMyLocationChangeListener(this);

        llDeviceController.setVisibility(View.GONE);

    }


    private void initEvent() {
        mainAmap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                deviceId = marker.getTitle();
                if (deviceId == null || deviceId.isEmpty()) {
                    return false;
                }
                homeHandler.sendEmptyMessage(MSG_UPDATE_ALARM_BTN);
                homeHandler.sendEmptyMessage(MSG_GET_FENCE_STATE);
                llDeviceController.setVisibility(View.VISIBLE);


                return false;
            }
        });

        btnControlAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enswitch) {
                    btnControlAlarm.setText(getString(R.string.str_close));
                    enswitch = false;

                } else {
                    btnControlAlarm.setText(getString(R.string.str_open));
                    enswitch = true;
                }
                DeviceState deviceState = new DeviceState();
                deviceState.setPower(enswitch);
                DeviceLocation.getInstance().sendRetainMessage("topic://" + deviceId + "/down/power",
                        LocationToJson.getStateJson(deviceState));
                EventBus.getDefault().post(new FreshUi());
            }

        });

        btnControlConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enConfig) {
                    btnControlConfig.setText(getString(R.string.str_close));
                    enConfig = false;

                } else {
                    btnControlConfig.setText(getString(R.string.str_open));
                    enConfig = true;
                }
                DeviceConfig deviceConfig = new DeviceConfig();
                deviceConfig.setConfig(enConfig);
                DeviceLocation.getInstance().sendRetainMessage("topic://" + deviceId + "/down/config",
                        LocationToJson.getJson(deviceConfig));
                EventBus.getDefault().post(new FreshUi());
            }
        });


        btnCloseWindows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llDeviceController.setVisibility(View.GONE);
            }
        });
        btnControlFence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!enopen) {
                    ProgressDialogUtils.getInstance().show(getActivity(), R.string.str_set_fence);
                    String radis = etFenceValue.getText().toString();
                    int mradis = 0;
                    try {
                        if (radis.matches("[0-9]*")) {
                            mradis = Integer.parseInt(radis);
                        } else {
                            ToastUtils.makeShortText(getString(R.string.str_is_not_num), getActivity());
                            ProgressDialogUtils.getInstance().dismiss();
                            return;

                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        ToastUtils.makeShortText(getString(R.string.str_is_not_num), getActivity());
                        ProgressDialogUtils.getInstance().dismiss();
                        return;
                    }
                    LatLng fenceLaLng = null;
                    if (deviceCurrentLocation.get(deviceId) != null) {
                        fenceLaLng = new LatLng(deviceCurrentLocation.get(deviceId).getLatitude(), deviceCurrentLocation.get(deviceId).getLongitude());
                    } else {
                        ToastUtils.makeShortText(getString(R.string.str_not_get_location), getActivity());
                        ProgressDialogUtils.getInstance().dismiss();
                        return;
                    }
                    if (deviceCircle.get(fence.getId()) == null) {
                        Circle circle = drawCircle(fenceLaLng, mradis);
                        deviceCircle.put(fence.getId(), circle);
                    } else {
                        deviceCircle.get(fence.getId()).setRadius(mradis);
                        deviceCircle.get(fence.getId()).setCenter(fenceLaLng);
                    }
                    SendFenceBean sendFence = new SendFenceBean();
                    sendFence.setId(deviceId);
                    sendFence.setLatitude(fenceLaLng.latitude);
                    sendFence.setLongitude(fenceLaLng.longitude);
                    sendFence.setRadius((double) mradis);
                    sendFence.setState(true);
                    DeviceController.getInstance().sendFenceInfo(sendFence, fenceListener);

                } else {
                    LatLng fenceLaLng = new LatLng(23, 110);
                    if (deviceCurrentLocation.get(deviceId) != null) {
                        fenceLaLng = new LatLng(deviceCurrentLocation.get(deviceId).getLatitude(), deviceCurrentLocation.get(deviceId).getLongitude());
                    }
                    SendFenceBean sendFence = new SendFenceBean();
                    sendFence.setId(deviceId);
                    sendFence.setLatitude(fenceLaLng.latitude);
                    sendFence.setLongitude(fenceLaLng.latitude);
                    sendFence.setRadius((double) 10);
                    sendFence.setState(false);
                    DeviceController.getInstance().sendFenceInfo(sendFence, fenceCancelListener);
                }

            }
        });
    }

    @Subscribe
    public void getFence(FenceInfo fenceinfo) {
//        ToastUtils.makeShortText("dddd",FenceActivity.this);
        if (fenceinfo.getResult().getQueryParam() == null) {
            btnControlFence.setText(getString(R.string.str_close));
            enopen = false;
        } else {
            if (fenceinfo.getResult().getQueryParam().getId().equals(deviceId)) {
                if (fenceinfo.getStatus() == 1) {
                    fence = fenceinfo.getResult().getQueryParam();
                    if (fence.isState()) {
                        etFenceValue.setText(((int) (double) fence.getRadius()) + "");
                        if (deviceCircle.get(fence.getId()) == null) {
                            Circle circle = drawCircle(new LatLng(fence.getLatitude(),
                                    fence.getLongitude()), (int) (double) fence.getRadius());
                            deviceCircle.put(fence.getId(), circle);
                        } else {
                            deviceCircle.get(fence.getId()).setRadius(fence.getRadius().intValue());
                            deviceCircle.get(fence.getId()).setCenter(new LatLng(fence.getLatitude(),
                                    fence.getLongitude()));
                        }
                        etFenceValue.setEnabled(false);
                        btnControlFence.setText(getString(R.string.str_close));
                        enopen = true;
                    } else {
                        btnControlFence.setText(getString(R.string.str_open));
                        etFenceValue.setEnabled(true);
                        enopen = false;
                    }
                }
            }
        }

    }

    private Circle drawCircle(LatLng latlng, int raduis) {
        return mainAmap.addCircle(new CircleOptions().
                center(latlng).
                radius(raduis).
                fillColor(Color.argb(50, 1, 1, 1)).
                strokeColor(Color.argb(50, 1, 1, 1)).
                strokeWidth(15));

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateLocation(CurrentLocation currentLocation) {
        LatLng latlng = new LatLng(currentLocation.getLatitude()
                , currentLocation.getLongitude());
        String deviceId = currentLocation.getDeviceId();
        deviceCurrentLocation.put(currentLocation.getDeviceId(), currentLocation);
        if (deviceMap.get(currentLocation.getDeviceId()) == null) {
            MarkerOptions markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_car))
                    .position(latlng)
                    .draggable(true)
                    .title(currentLocation.getDeviceId())
                    .rotateAngle(currentLocation.getBearing());


            Marker marker = mainAmap.addMarker(markerOption);
            deviceMap.put(currentLocation.getDeviceId(), marker);

        } else {
            deviceMap.get(currentLocation.getDeviceId()).setPosition(latlng);
            deviceMap.get(currentLocation.getDeviceId()).setRotateAngle(currentLocation.getBearing());
            deviceMap.get(currentLocation.getDeviceId()).setTitle(currentLocation.getDeviceId());
        }


        if (deviceCircle.get(deviceId) != null) {
            if (!deviceCircle.get(deviceId).contains(latlng)) {
                NotificationManager notifyManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notification = new NotificationCompat.Builder(getActivity())
                        //设置小图标
                        .setSmallIcon(R.drawable.ic_priority_high_black_24dp)
                        //设置通知标题
                        .setContentTitle(getString(R.string.str_alarm))
                        //设置通知内容
                        .setContentText(getString(R.string.str_device_id) + currentLocation.getDeviceId()
                                + getString(R.string.str_out_fence))
                        .setDefaults(Notification.DEFAULT_ALL)
                        .build();
                notifyManager.notify(100, notification);
            }
        }




    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void unbindDeivce(UnbindDevice unbindDevice) {
        String deviceId = unbindDevice.getDeviceId();
        for (int i = 0; i < deviceMap.size(); i++) {
            deviceMap.get(deviceId).destroy();
            deviceMap.remove(deviceId);

        }
        for (int j = 0; j < deviceCircle.size(); j++) {
            deviceCircle.get(deviceId).remove();
            deviceMap.remove(deviceId);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mainMapview.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mainMapview.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mainMapview.onSaveInstanceState(outState);
    }


    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
        mainMapview.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onMyLocationChange(Location location) {
//        DeviceLocation.getInstance().sendLocation(Config.TOPICSEND,LocationToJson.getJson(location));
    }


}

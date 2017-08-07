package com.sciento.wumu.gpscontroller.HomeModule;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.sciento.wumu.gpscontroller.ConfigModule.Config;
import com.sciento.wumu.gpscontroller.MqttModule.CurrentLocation;
import com.sciento.wumu.gpscontroller.MqttModule.DeviceLocation;
import com.sciento.wumu.gpscontroller.MqttModule.LocationToJson;
import com.sciento.wumu.gpscontroller.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class HomeFragment extends Fragment implements
        AMap.OnMyLocationChangeListener
{


    @BindView(R.id.main_mapview)
    TextureMapView mainMapview;
    Unbinder unbinder;



    private AMap mainAmap =null;
    private UiSettings mUiSettings;
    private LocationSource.OnLocationChangedListener mLocationChangedListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;

    private HashMap<String,Marker> deviceMap = new HashMap<>();


    Handler homeHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){

            }
            super.handleMessage(msg);
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
        if(mainAmap == null) {
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


    }



    private void initEvent() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateLocation(CurrentLocation currentLocation){
        LatLng latlng = new LatLng(currentLocation.getLatitude()
                , currentLocation.getLongitude());

        if(deviceMap.get(currentLocation.getDeviceId()) == null){
            MarkerOptions markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.icon_car))
                    .position(latlng)
                    .draggable(true);

            Marker marker = mainAmap.addMarker(markerOption);
            marker.setRotateAngle(currentLocation.getBearing());
            deviceMap.put(currentLocation.getDeviceId(),marker);

        }else {
            deviceMap.get(currentLocation.getDeviceId()).setPosition(latlng);
            deviceMap.get(currentLocation.getDeviceId()).setRotateAngle(currentLocation.getBearing());
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

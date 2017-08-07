package com.sciento.wumu.gpscontroller.TestModule;

import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
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

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.internal.ConnectActionListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class ShowInfoFragment extends Fragment implements
        AMap.OnMyLocationChangeListener
{


    @BindView(R.id.main_mapview)
    TextureMapView mainMapview;
    @BindView(R.id.btn_showmarker)
    Button btn_showmarker;
    Unbinder unbinder;



    private AMap mainAmap =null;
    private UiSettings mUiSettings;
    private LocationSource.OnLocationChangedListener mLocationChangedListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;


    IMqttToken subToken;

    Marker marker;
    private MarkerOptions markerOption;
    private LatLng latlng = new LatLng(39.761, 116.434);
    final int MSG_ADD_MARKER = 333;

    private View view;

    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_ADD_MARKER:
                    Bundle bundle = msg.getData();
                    latlng = new LatLng(bundle.getDouble("Latitude")
                            , bundle.getDouble("Longitude"));
                    if(marker ==null){
                        markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_car))
                                .position(latlng)
                                .draggable(true);
                        marker = mainAmap.addMarker(markerOption);
                    }else {
                        marker.setPosition(latlng);
                    }
                    //marker.destroy();
                    Toast.makeText(getActivity(),bundle.getDouble("Longitude")+"",Toast.LENGTH_SHORT).show();

                    break;
            }
        }
    };

    public ShowInfoFragment() {
        // Required empty public constructor
    }

    @OnClick(R.id.btn_showmarker)
    void OnClick(Button button){
        markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_car))
                .position(latlng)
                .draggable(true);
        marker = mainAmap.addMarker(markerOption);

    }



    // TODO: Rename and change types and number of parameters
    public static ShowInfoFragment newInstance() {
        ShowInfoFragment fragment = new ShowInfoFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_showinfo, container, false);
        unbinder = ButterKnife.bind(this, view);
        mainMapview.onCreate(savedInstanceState);// 此方法必须重写

            init();

        initEvent();
        return view;
    }

    private void init()  {
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
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        mainAmap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        mainAmap.setOnMyLocationChangeListener(this);


//            subToken = DeviceLocation.getInstance().getMqttAndroidClient()
//                                            .subscribe(Config.TOPICSEND, 0);
//            subToken.setActionCallback(new IMqttActionListener() {
//                @Override
//                public void onSuccess(IMqttToken asyncActionToken) {
//                    // The message was published
//                    //Toast.makeText(getActivity(),"ddddddd",Toast.LENGTH_SHORT).show();
//
//                }
//
//                @Override
//                public void onFailure(IMqttToken asyncActionToken,
//                                      Throwable exception) {
//                    // The subscription could not be performed, maybe the user was not
//                    // authorized to subscribe on the specified topic e.g. using wildcards
//
//                }
//            });


//        DeviceLocation.getInstance().getMqttAndroidClient().setCallback(new MqttCallback() {
//                @Override
//                public void connectionLost(Throwable cause) {
//                    Toast.makeText(getActivity(),"eeeeeee",Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void messageArrived(String topic, MqttMessage message) throws Exception {
//                     String strMessage = message.toString();
//                    Toast.makeText(getActivity(),strMessage,Toast.LENGTH_SHORT).show();
//                    CurrentLocation currentLocation = LocationToJson.getPojo(strMessage,
//                            CurrentLocation.class);
//
////                    LatLng latLng = new LatLng(currentLocation.getLongitude()
////                            , currentLocation.getAltitude());
//
//
//                    Message msg = Message.obtain();
//                    msg.what = MSG_ADD_MARKER;
//                    Bundle bundle =new Bundle();
//                    bundle.putDouble("Longitude",currentLocation.getLongitude());
//                    bundle.putDouble("Latitude",currentLocation.getLatitude());
//                    msg.setData(bundle);
//                    handler.sendMessage(msg);




//                }

//                @Override
//                public void deliveryComplete(IMqttDeliveryToken token) {

//                }
//            });
//
//        try {
//            DeviceLocation.getInstance().getMqttAndroidClient().subscribe(Config.TOPICSEND, 0);
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }


    }



    private void initEvent() {

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
        super.onDestroyView();
        mainMapview.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onMyLocationChange(Location location) {


        DeviceLocation.getInstance().sendLocation(Config.TOPICSEND,LocationToJson.getJson(location));
    }




}

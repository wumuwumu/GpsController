package com.sciento.wumu.gpscontroller.DeviceModule;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sciento.wumu.gpscontroller.CommonModule.AppContext;
import com.sciento.wumu.gpscontroller.ConfigModule.Config;
import com.sciento.wumu.gpscontroller.ConfigModule.UserState;
import com.sciento.wumu.gpscontroller.ControllerModule.ControllerActivity;
import com.sciento.wumu.gpscontroller.DeviceSdk.DeciceBean;
import com.sciento.wumu.gpscontroller.DeviceSdk.DevicePlus;
import com.sciento.wumu.gpscontroller.DeviceSdk.DeviceController;
import com.sciento.wumu.gpscontroller.DeviceSdk.DeviceListAdapter;
import com.sciento.wumu.gpscontroller.DeviceSdk.DeviceListener;
import com.sciento.wumu.gpscontroller.DeviceSdk.ErrorCode;
import com.sciento.wumu.gpscontroller.Event.DeviceConnected;
import com.sciento.wumu.gpscontroller.Event.DeviceDisconnect;
import com.sciento.wumu.gpscontroller.Model.JsonDevice;
import com.sciento.wumu.gpscontroller.MqttModule.CurrentLocation;
import com.sciento.wumu.gpscontroller.MqttModule.LocationToJson;
import com.sciento.wumu.gpscontroller.R;
import com.sciento.wumu.gpscontroller.Utils.ProgressDialogUtils;
import com.sciento.wumu.gpscontroller.Utils.ToastUtils;
import com.sciento.wumu.gpscontroller.View.SlideListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * this is about DevicePlus <br>
 * A simple {@link Fragment} subclass.
 * Use the {@link DeviceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeviceFragment extends DeviceBaseFragment {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    Unbinder unbinder;

    //声明相关的变量
    private View icBundleDevice;
    private View icOfflineDevice;

    private TextView tvBundleDeviceStatus;
    private TextView tvOfflineDeviceStatus;

    private LinearLayout llBundleDevice;
    private LinearLayout llOfflineDevice;

    private SlideListView slvBundleDevice;
    private SlideListView slvOfflineDevice;

    private NestedScrollView nestedScrollView;

    private SwipeRefreshLayout swipeRefreshLayout;

    //Handler message
    private final int MSG_GET_DEVICE_LIST = 10;
    private final int MSG_UPDATE_DEVICE_LIST = 11;
    private final int MSG_CONTTROLLER = 12;
    private final int MSG_BOUND = 13;
    private final int MSG_UNBOUND = 14;
    private final int MSG_UPDATEUI = 15;
    private final int MSG_SWIPE_REFRESH = 16;
    private final int MSG_TEST = 33;


    //
    public static List<String> boundMessage = new ArrayList<>();

    List<DevicePlus> boundDeviceList;
    List<DevicePlus> offlineDevicesList;


    private DeviceListAdapter deviceListAdapter;

    private String userphone;
    private String token;


    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_GET_DEVICE_LIST:
//                    if(!userphone.isEmpty() && !token.isEmpty()){
//
//                    }
                    DeviceController.getInstance().getAllDeviceList(userphone,token);

                    break;

                case MSG_UPDATEUI:
                    updateUi();
                    break;
                case MSG_UNBOUND:
                    ProgressDialogUtils.getInstance().show(getActivity(),
                            getString(R.string.str_unbind_device));
                    DeviceController.getInstance().unBindDevice(userphone,token,msg.obj.toString());
                    break;
                case MSG_UPDATE_DEVICE_LIST:
                    ProgressDialogUtils.getInstance().dismiss();
                    updateUi();
                    break;
                case MSG_SWIPE_REFRESH:
                    handler.sendEmptyMessage(MSG_GET_DEVICE_LIST);
                    swipeRefreshLayout.setRefreshing(false);
                    break;

                case MSG_CONTTROLLER:
//                    ToastUtils.makeShortText("jjjjjjj",getActivity());
                    Bundle bundle =new Bundle();
                    bundle.putString("deviceid",msg.obj.toString());
                    Intent controllerIntent = new Intent(getActivity(), ControllerActivity.class);
                    controllerIntent.putExtras(bundle);
                    startActivity(controllerIntent);
                    break;
                case MSG_TEST:
                    ToastUtils.makeShortText("yoyxiaoxi",getActivity());
                    break;

            }
            super.handleMessage(msg);
        }
    };

    DeviceListener deviceListener = new DeviceListener(){

        @Override
        public void didSubscribeState(DevicePlus device, int result) {

            ToastUtils.makeShortText(""+result,getActivity());
        }

        @Override
        public void didInfo(String info) {
            ToastUtils.makeShortText(info,getActivity());
        }

        @Override
        public void didUpdateLocation(String deviceid, CurrentLocation currentLocation) {
            ToastUtils.makeShortText(deviceid,getActivity());
        }
    };


    public static DeviceFragment newInstance() {
        DeviceFragment fragment = new DeviceFragment();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_device, container, false);
        unbinder = ButterKnife.bind(this, view);

        handler.sendEmptyMessage(MSG_GET_DEVICE_LIST);
        initData();
        initView(view);
        initEvent();

        setHasOptionsMenu(true);


        return view;
    }

    private void initData() {
//        userphone = sharedPreferences.getString("Uid", "");
//        token = sharedPreferences.getString("Token", "");
//
//        if (userphone.isEmpty() && token.isEmpty()) {
//            UserState.issignin = false;
//        }

    }

    private void initEvent() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.sendEmptyMessage(MSG_SWIPE_REFRESH);
            }
        });

        slvBundleDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ProgressDialogUtils.getInstance().show(getActivity(),
//                        getString(R.string.str_start_subscribe));
//                slvBundleDevice.setEnabled(false);
//                slvBundleDevice.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        slvBundleDevice.setEnabled(true);
//                    }
//                },3000);

                ToastUtils.makeShortText("dddsdfasfsf",getActivity());
//                device.setDeviceListener(getDeviceListener());

               //没有完成
            }
        });
    }

    private void initView(View view) {
        //toolbar
        toolbar.setTitle(R.string.str_my_device);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        //init view
        icBundleDevice = (View) view.findViewById(R.id.ic_bound_devices);
        icOfflineDevice = (View) view.findViewById(R.id.ic_offline_devices);

        tvBundleDeviceStatus = (TextView) icBundleDevice.findViewById(R.id.tv_device_staus);
        tvOfflineDeviceStatus = (TextView) icOfflineDevice.findViewById(R.id.tv_device_staus);

        llBundleDevice = (LinearLayout) icBundleDevice.findViewById(R.id.ll_no_device);
        llOfflineDevice = (LinearLayout) icOfflineDevice.findViewById(R.id.ll_no_device);

        slvBundleDevice = (SlideListView) icBundleDevice.findViewById(R.id.slideListView);
        slvOfflineDevice = (SlideListView) icOfflineDevice.findViewById(R.id.slideListView);

        nestedScrollView = (NestedScrollView) view.findViewById(R.id.netstedscrollview);

        //init value
        tvBundleDeviceStatus.setText(getString(R.string.str_bundle_device));
        tvOfflineDeviceStatus.setText(getString(R.string.str_offline_device));


        //swipe refresh
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefreshlayout);

        slvBundleDevice.initSlideMode(SlideListView.MOD_RIGHT);
        slvOfflineDevice.initSlideMode(SlideListView.MOD_RIGHT);
    }

    private void updateUi() {
        boundDeviceList = new ArrayList<>();
        offlineDevicesList = new ArrayList<>();

        for(DevicePlus device : DeviceBaseFragment.deviceslist){
            if(device.getJsonDevice().isOnline() == true){
                boundDeviceList.add(device);
            }else {
                offlineDevicesList.add(device);
            }
        }

        if(boundDeviceList.isEmpty()){
            slvBundleDevice.setVisibility(View.GONE);
            llBundleDevice.setVisibility(View.VISIBLE);
        }else {
            deviceListAdapter = new DeviceListAdapter(getActivity(), boundDeviceList);
            deviceListAdapter.setHandler(handler);
            slvBundleDevice.setAdapter(deviceListAdapter);
            slvBundleDevice.setVisibility(View.VISIBLE);
            llBundleDevice.setVisibility(View.GONE);
        }

        if(offlineDevicesList.isEmpty()){
            slvOfflineDevice.setVisibility(View.GONE);
            llOfflineDevice.setVisibility(View.VISIBLE);
        }else {
            deviceListAdapter = new DeviceListAdapter(getActivity(), offlineDevicesList);
            deviceListAdapter.setHandler(handler);
            slvOfflineDevice.setAdapter(deviceListAdapter);
            slvOfflineDevice.setVisibility(View.VISIBLE);
            llOfflineDevice.setVisibility(View.GONE);
        }

    }

    @Override
    public void onStart() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        updateUi();
        if (boundMessage.size() != 0) {
            ProgressDialogUtils.getInstance().show(getActivity(), getString(R.string.str_add_device));
            DeviceController.getInstance().bindDevice(UserState.username, token, boundMessage.get(0));

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        boundMessage.clear();
    }

    @Override
    public void onStop() {

        super.onStop();
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
        unbinder.unbind();
    }

    @Subscribe
    public void onEvent(String event) {
        //Toast.makeText(getActivity(), event.toString(), Toast.LENGTH_SHORT).show();
        boundMessage.clear();
        boundMessage.add(event);

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void inInfo(DeciceBean deciceBean){
        handler.sendEmptyMessage(MSG_TEST);
        Toast.makeText(getActivity(),deciceBean.getDeviceId(), Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getDeviceInfo(CurrentLocation currentLocation){
//        handler.sendEmptyMessage(MSG_TEST);
        Toast.makeText(getActivity(),currentLocation.getDeviceId()+currentLocation.getAltitude(),
                Toast.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getDeviceConnected(DeviceConnected deviceConnected){
        String deviceId = deviceConnected.getDeviceId();
        ListIterator<DevicePlus> deviceListIterator = DeviceBaseFragment.deviceslist.listIterator();
        while (deviceListIterator.hasNext()){
            if(deviceListIterator.next().getJsonDevice().getId().equals(deviceId)){
                deviceListIterator.next().getJsonDevice().setOnline(true);
                handler.sendEmptyMessage(MSG_UPDATEUI);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getDeviceDisconnect(DeviceDisconnect deviceDisconnect){
        String deviceId = deviceDisconnect.getDeviceId();
        ListIterator<DevicePlus> deviceListIterator = DeviceBaseFragment.deviceslist.listIterator();
        while (deviceListIterator.hasNext()){
            if(deviceListIterator.next().getJsonDevice().getId().equals(deviceId)){
                deviceListIterator.next().getJsonDevice().setOnline(false);
                handler.sendEmptyMessage(MSG_UPDATEUI);
            }
        }
    }




    @Override
    public void DidGetAllDevice(int errorcode, List<DevicePlus> devices) {
        if(errorcode == ErrorCode.CODE_SUCCESS){
            DeviceBaseFragment.deviceslist.clear();
            for (DevicePlus device : devices) {
                DeviceBaseFragment.deviceslist.add(device);
                device.setDeviceListener(deviceListener);
                device.setSubscribe(true);
            }
            handler.sendEmptyMessage(MSG_UPDATE_DEVICE_LIST);
            Toast.makeText(getActivity(), changeErrorCodeToString(errorcode), Toast.LENGTH_SHORT)
                    .show();
        }else {
            Toast.makeText(getActivity(), changeErrorCodeToString(errorcode), Toast.LENGTH_SHORT)
                    .show();
        }

    }

    @Override
    public void DidBindDevice(int errorcode) {
//        ProgressDialogUtils.getInstance().dismiss();
        if (ErrorCode.CODE_SUCCESS != errorcode) {
            Toast.makeText(getActivity(), changeErrorCodeToString(errorcode), Toast.LENGTH_SHORT)
                    .show();
        }else {
            Toast.makeText(getActivity(), changeErrorCodeToString(errorcode), Toast.LENGTH_SHORT)
                    .show();
        }

    }

    @Override
    public void DidUnbindDevice(int errorcode) {
//        ProgressDialogUtils.getInstance().dismiss();
        if (ErrorCode.CODE_SUCCESS != errorcode) {
            Toast.makeText(getActivity(), changeErrorCodeToString(errorcode), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void DidUpdataDevice(int errorcode) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_device, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_qrcode:
                Intent qrcodeIntent = new Intent(getActivity(), QRcodeActivity.class);
                startActivity(qrcodeIntent);
                break;
            case R.id.action_test:
                String url =Config.HTTPSERVER+"/api/bind";

                Map<String, String>map = new HashMap<>();
                map.put("userName",UserState.username);
                map.put("deviceId","123435678888888888888888886");
                JSONObject jsonObject = new JSONObject(map);
                // 参数：[请求方式][请求链接][请求参数][成功回调][失败回调]
                JsonObjectRequest mStringRequest = new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                int status  =-2;
                                String str ="1";
                                try {
                                    status = response.getInt("status");
                                    str = response.getString("msg");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if(status ==1){
                                    ToastUtils.makeShortText("ss",getActivity());
                                }
                                ToastUtils.makeShortText(str,getActivity());



                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                               ToastUtils.makeShortText("err",getActivity());
                            }
                        }
                ){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("Cookie" , UserState.referer );
                        return params;
                    }
                };

                mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        20 * 1000, 3, 1.0f));
                mStringRequest.setTag("bind");// 设置标签
                AppContext.getRequestQueue().add(mStringRequest);// 将请求添加进队列
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

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

import com.sciento.wumu.gpscontroller.DeviceSdk.Device;
import com.sciento.wumu.gpscontroller.DeviceSdk.DeviceController;
import com.sciento.wumu.gpscontroller.DeviceSdk.DeviceListAdapter;
import com.sciento.wumu.gpscontroller.DeviceSdk.ErrorCode;
import com.sciento.wumu.gpscontroller.R;
import com.sciento.wumu.gpscontroller.Utils.ProgressDialogUtils;
import com.sciento.wumu.gpscontroller.View.SlideListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * this is about Device <br>
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


    //
    public static List<String> boundMessage = new ArrayList<>();

    List<Device> boundDeviceList;
    List<Device> offlineDevicesList;


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
                    handler.sendEmptyMessage(MSG_SWIPE_REFRESH);
                    swipeRefreshLayout.setRefreshing(false);
                    break;

                case MSG_CONTTROLLER:

                    break;

            }
            super.handleMessage(msg);
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
                ProgressDialogUtils.getInstance().show(getActivity(),
                        getString(R.string.str_start_subscribe));
                slvBundleDevice.setEnabled(false);
                slvBundleDevice.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        slvBundleDevice.setEnabled(true);
                    }
                },3000);

                Device device = boundDeviceList.get(position);
                device.setDeviceListener(getDeviceListener());

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

    }

    private void updateUi() {
        boundDeviceList = new ArrayList<>();
        offlineDevicesList = new ArrayList<>();

        for(Device device : DeviceBaseFragment.deviceslist){
            if(device.getStatus() == true){
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
            DeviceController.getInstance().bindDevice(userphone, token, boundMessage.get(0));

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
        Toast.makeText(getActivity(), event.toString(), Toast.LENGTH_SHORT).show();
        boundMessage.add(event);

    }


    @Override
    public void DidGetAllDevice(int errorcode, List<Device> devices) {
        DeviceBaseFragment.deviceslist.clear();
        for (Device gizWifiDevice : devices) {
            DeviceBaseFragment.deviceslist.add(gizWifiDevice);
        }
        handler.sendEmptyMessage(MSG_UPDATE_DEVICE_LIST);
    }

    @Override
    public void DidBindDevice(int errorcode) {
        ProgressDialogUtils.getInstance().dismiss();
        if (ErrorCode.CODE_SUCCESS != errorcode) {
            Toast.makeText(getActivity(), changeErrorCodeToString(errorcode), Toast.LENGTH_SHORT)
                    .show();
        }else {
            Toast.makeText(getActivity(),getString(R.string.str_bind_success),Toast.LENGTH_SHORT)
                    .show();
        }

    }

    @Override
    public void DidUnbindDevice(int errorcode) {
        ProgressDialogUtils.getInstance().dismiss();
        if (ErrorCode.CODE_SUCCESS != errorcode) {
            Toast.makeText(getActivity(), changeErrorCodeToString(errorcode), Toast.LENGTH_SHORT)
                    .show();
        }
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
                if (boundMessage.isEmpty())
                    Toast.makeText(getActivity(), "//he", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getActivity(), "//he" + boundMessage.toString(), Toast.LENGTH_SHORT).show();

                //EventBus.getDefault().post("ddddd");
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

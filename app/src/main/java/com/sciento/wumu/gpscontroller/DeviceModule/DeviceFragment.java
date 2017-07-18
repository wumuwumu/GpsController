package com.sciento.wumu.gpscontroller.DeviceModule;


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
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sciento.wumu.gpscontroller.ConfigModule.UserState;
import com.sciento.wumu.gpscontroller.DeviceSdk.Device;
import com.sciento.wumu.gpscontroller.R;
import com.sciento.wumu.gpscontroller.View.NoScrollViewPager;
import com.sciento.wumu.gpscontroller.View.SlideListView;

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


    //
    public static List<String> boundMessage;

    private String userphone;
    private String token;




    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_GET_DEVICE_LIST:
                    if(!userphone.isEmpty() && !token.isEmpty()){

                    }


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
        userphone = sharedPreferences.getString("Uid", "");
        token = sharedPreferences.getString("Token", "");

        if (userphone.isEmpty() && token.isEmpty()) {
            UserState.issignin = false;
        }

    }

    private void initEvent() {
    }

    private void initView(View view) {
        //toolbar
        toolbar.setTitle(R.string.str_my_device);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        //init view
        icBundleDevice = (View)view.findViewById(R.id.ic_bound_devices);
        icOfflineDevice = (View)view.findViewById(R.id.ic_offline_devices);

        tvBundleDeviceStatus = (TextView)icBundleDevice.findViewById(R.id.tv_device_staus);
        tvOfflineDeviceStatus = (TextView)icOfflineDevice.findViewById(R.id.tv_device_staus);

        llBundleDevice = (LinearLayout)icBundleDevice.findViewById(R.id.ll_no_device);
        llOfflineDevice= (LinearLayout)icOfflineDevice.findViewById(R.id.ll_no_device);

        slvBundleDevice = (SlideListView)icBundleDevice.findViewById(R.id.slideListView);
        slvOfflineDevice = (SlideListView)icOfflineDevice.findViewById(R.id.slideListView);

        nestedScrollView = (NestedScrollView)view.findViewById(R.id.netstedscrollview);

        //init value
        tvBundleDeviceStatus.setText(getString(R.string.str_bundle_device));
        tvOfflineDeviceStatus.setText(getString(R.string.str_offline_device));


        //swipe refresh
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swiperefreshlayout);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void getAllDevice(int errorcode, List<Device> devices) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_device, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}

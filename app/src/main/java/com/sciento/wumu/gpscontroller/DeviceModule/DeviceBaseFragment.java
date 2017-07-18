package com.sciento.wumu.gpscontroller.DeviceModule;

import android.support.v4.app.Fragment;

import com.sciento.wumu.gpscontroller.DeviceSdk.Device;
import com.sciento.wumu.gpscontroller.DeviceSdk.DeviceController;
import com.sciento.wumu.gpscontroller.DeviceSdk.DeviceControllerListener;

import java.util.List;

/**
 * Created by wumu on 17-7-16.
 */

public class DeviceBaseFragment extends BaseFragment{



    DeviceControllerListener deviceControllerListener = new DeviceControllerListener() {
        @Override
        public void getAllDevice(int errorCode, List<Device> devices) {
            DeviceBaseFragment.this.getAllDevice( errorCode,  devices);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        DeviceController.getInstance().setDeviceControllerListener(deviceControllerListener);
    }

    public void getAllDevice(int errorcode, List<Device> devices){}
}

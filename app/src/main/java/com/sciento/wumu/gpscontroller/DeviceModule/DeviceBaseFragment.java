package com.sciento.wumu.gpscontroller.DeviceModule;

import android.support.v4.app.Fragment;
import android.view.View;

import com.sciento.wumu.gpscontroller.DeviceSdk.Device;
import com.sciento.wumu.gpscontroller.DeviceSdk.DeviceController;
import com.sciento.wumu.gpscontroller.DeviceSdk.DeviceControllerListener;
import com.sciento.wumu.gpscontroller.DeviceSdk.DeviceListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wumu on 17-7-16.
 */

public class DeviceBaseFragment extends BaseFragment{

    protected static List<Device> deviceslist = new ArrayList<Device>();

    DeviceControllerListener deviceControllerListener = new DeviceControllerListener() {
        @Override
        public void DidGetAllDevice(int errorCode, List<Device> devices) {
            DeviceBaseFragment.this.DidGetAllDevice( errorCode,  devices);
        }

        @Override
        public void DidBindDevice(int errorcode) {
            DeviceBaseFragment.this.DidBindDevice(errorcode);
        }

        @Override
        public void DidUnbindDivce(int errorcode) {
            DeviceBaseFragment.this.DidUnbindDevice(errorcode);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        DeviceController.getInstance().setDeviceControllerListener(deviceControllerListener);
    }

    public void DidGetAllDevice(int errorcode, List<Device> devices){}

    public void DidBindDevice(int errorcode){}

    public void DidUnbindDevice(int errorcode){}



    //device listener
//    protected DeviceListener deviceListener = new DeviceListener(){
//        @Override
//        public void didDisConnected(Device device, int result) {
//            super.didDisConnected(device, result);
//        }
//
//        @Override
//        public void didOnline(Device device, int result) {
//            super.didOnline(device, result);
//        }
//
//        @Override
//        public void didSetSubscribe(int errorcode, Device device, int issubscribe) {
//            super.didSetSubscribe(errorcode, device, issubscribe);
//        }
//    };
//
//
//    protected DeviceListener getDeviceListener(){
//        return deviceListener;
//    }

}

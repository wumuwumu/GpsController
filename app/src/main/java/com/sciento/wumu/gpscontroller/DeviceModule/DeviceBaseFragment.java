package com.sciento.wumu.gpscontroller.DeviceModule;

import com.sciento.wumu.gpscontroller.DeviceSdk.DevicePlus;
import com.sciento.wumu.gpscontroller.DeviceSdk.DeviceController;
import com.sciento.wumu.gpscontroller.DeviceSdk.DeviceControllerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wumu on 17-7-16.
 */

public class DeviceBaseFragment extends BaseFragment{

    public static List<DevicePlus> deviceslist = new ArrayList<DevicePlus>();

    DeviceControllerListener deviceControllerListener = new DeviceControllerListener() {
        @Override
        public void DidGetAllDevice(int errorCode, List<DevicePlus> devices) {
            DeviceBaseFragment.this.DidGetAllDevice( errorCode,  devices);
        }

        @Override
        public void DidBindDevice(int errorcode) {
            DeviceBaseFragment.this.DidBindDevice(errorcode);
        }

        @Override
        public void DidUnbindDevice(int errorcode) {
            DeviceBaseFragment.this.DidUnbindDevice(errorcode);
        }

        @Override
        public void DidUpdateDevice(int errorcode) {
            DidUpdateDevice(errorcode);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        DeviceController.getInstance().setDeviceControllerListener(deviceControllerListener);
    }

    public void DidGetAllDevice(int errorcode, List<DevicePlus> devices){}

    public void DidBindDevice(int errorcode){}

    public void DidUnbindDevice(int errorcode){}

    public void DidUpdataDevice(int errorcode){}



    //device listener
//    protected DeviceListener deviceListener = new DeviceListener(){
//        @Override
//        public void didDisConnected(DevicePlus device, int result) {
//            super.didDisConnected(device, result);
//        }
//
//        @Override
//        public void didOnline(DevicePlus device, int result) {
//            super.didOnline(device, result);
//        }
//
//        @Override
//        public void didSetSubscribe(int errorcode, DevicePlus device, int issubscribe) {
//            super.didSetSubscribe(errorcode, device, issubscribe);
//        }
//    };
//
//
//    protected DeviceListener getDeviceListener(){
//        return deviceListener;
//    }

}

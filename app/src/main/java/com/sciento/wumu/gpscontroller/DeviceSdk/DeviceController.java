package com.sciento.wumu.gpscontroller.DeviceSdk;

/**
 * Created by wumu on 17-7-17.
 */

public class DeviceController {
    private DeviceController(){}
    private static DeviceController deviceController = new DeviceController();

    private  DeviceControllerListener deviceControllerListener;






    public synchronized static DeviceController getInstance(){
        return  deviceController;
    }


    public void setDeviceControllerListener(DeviceControllerListener deviceControllerListener){
        this.deviceControllerListener = deviceControllerListener;
    }

}

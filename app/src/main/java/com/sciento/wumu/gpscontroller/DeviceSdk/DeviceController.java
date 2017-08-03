package com.sciento.wumu.gpscontroller.DeviceSdk;

/**
 * Created by wumu on 17-7-17.
 */

public class DeviceController {

    private static DeviceController deviceController = new DeviceController();

    private  DeviceControllerListener deviceControllerListener;
    private  SdkEventController sdkEventController;


    private DeviceController(){
        sdkEventController = SdkEventController.getInstance();
    }





    public synchronized static DeviceController getInstance(){
        return  deviceController;
    }


    public void setDeviceControllerListener(DeviceControllerListener deviceControllerListener){
        this.deviceControllerListener = deviceControllerListener;
        sdkEventController.setDeviceControllerListener(deviceControllerListener);
    }

    public void bindDevice(String userphone ,String token, String deviceId){
        sdkEventController.bindDevice(userphone,token,deviceId);
    }

    public void unBindDevice(String userphone ,String token, String deviceId){
        sdkEventController.unBindDevice(userphone,token,deviceId);
    }

    public void  getAllDeviceList(String userphone , String token){
        sdkEventController.getAllDeviceList(userphone,token);
    }

    public void updataDevice(String deviceId,boolean isonline){
        sdkEventController.updataDevice(deviceId,isonline);
    }

}

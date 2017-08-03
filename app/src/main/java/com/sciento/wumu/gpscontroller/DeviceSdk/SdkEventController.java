package com.sciento.wumu.gpscontroller.DeviceSdk;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by wumu on 17-7-17.
 */

public class SdkEventController {
    private static final SdkEventController sdkEventController = new SdkEventController();
    private static DeviceControllerListener deviceControllerListener;
    private static List<Device> deviceArrayList =  new ArrayList<>();


    Handler handler = new Handler(Looper.getMainLooper()){

        @Override
        public void handleMessage(Message msg) {

        }
    };

    //set listener
    public void setDeviceControllerListener(DeviceControllerListener deviceControllerListener){
        this.deviceControllerListener = deviceControllerListener;
    }



    public synchronized static SdkEventController getInstance(){
        return sdkEventController;
    }

    public void bindDevice(String userphone,String token ,String deviceId){
        ListIterator<Device> deviceListIterator = deviceArrayList.listIterator();
        boolean enGo = true;
        while (deviceListIterator.hasNext()){
            if(deviceListIterator.next().getDeviceId().equals(deviceId)){
                onDidBindDevice(ErrorCode.CODE_BIND_FAIL);
                enGo=false;
                break;
            }
        }

        if(enGo){
            Device device = new Device();
            device.setDeviceId(deviceId);
            deviceArrayList.add(device);
            onDidGetAllDevice(ErrorCode.CODE_SUCCESS,deviceArrayList);
            onDidBindDevice(ErrorCode.CODE_SUCCESS);
        }

        //send server


    }


    public void unBindDevice(String userphone,String token ,String deviceid){
        ListIterator<Device> deviceIterable = deviceArrayList.listIterator();
        boolean enGo = true;
        while(deviceIterable.hasNext()){
            if((deviceIterable.next().getDeviceId()).equals(deviceid)){
                deviceIterable.remove();
                enGo = false;

            }
        }

        if (!enGo){
            onDidUnbindDevice(ErrorCode.CODE_SUCCESS);
            onDidGetAllDevice(ErrorCode.CODE_SUCCESS,deviceArrayList);
        }else {
            onDidUnbindDevice(ErrorCode.CODE_UNBIND_FAIL);
        }
    }


    public void getAllDeviceList(String userphone,String token ){
        onDidGetAllDevice(ErrorCode.CODE_SUCCESS,deviceArrayList);
    }

    public void updataDevice(String deivceid,boolean isonline){
        ListIterator<Device> deviceIterable = deviceArrayList.listIterator();
        boolean enGo = true;
        while(deviceIterable.hasNext()){
            if((deviceIterable.next().getDeviceId()).equals(deivceid)){
                deviceIterable.next().setOnline(isonline);
                enGo = false;

            }
        }
        if(enGo){

        }
    }



    //listener
    protected void onDidGetAllDevice(int errorCode,List<Device> devices){
        deviceControllerListener.DidGetAllDevice(errorCode,devices);
    }

    protected void onDidBindDevice(int errorcode ){
        deviceControllerListener.DidBindDevice(errorcode);
    }

    protected void onDidUnbindDevice(int errorcode ){
        deviceControllerListener.DidUnbindDive(errorcode);
    }

}

package com.sciento.wumu.gpscontroller.DeviceSdk;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.List;

/**
 * Created by wumu on 17-7-17.
 */

public class SdkEventController {
    private static final SdkEventController sdkEventController = new SdkEventController();



    Handler handler = new Handler(Looper.getMainLooper()){

        @Override
        public void handleMessage(Message msg) {

        }
    };



    public synchronized static SdkEventController getInstance(){
        return sdkEventController;
    }

    public void bindDevice(String userphone,String token ,String deviceId){

    }


    public void unBindDevice(String userphone,String token ,String deviceId){

    }



    //listener
    public void onDidGetAllDevice(int errorCode,List<Device> devices){

    }

    public void onDidBindDevice(int errorcode ){

    }

    public void onDidUnbindDevice(int errorcode ){

    }

}

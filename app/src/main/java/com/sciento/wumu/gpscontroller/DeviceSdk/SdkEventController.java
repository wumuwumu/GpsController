package com.sciento.wumu.gpscontroller.DeviceSdk;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

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



    SdkEventController getInstance(){
        return sdkEventController;
    }

}

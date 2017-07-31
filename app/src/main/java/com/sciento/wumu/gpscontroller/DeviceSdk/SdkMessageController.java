package com.sciento.wumu.gpscontroller.DeviceSdk;

/**
 * Created by wumu on 17-7-21.
 */

public class SdkMessageController {
    private static SdkMessageController sdkMessageController;
    private SdkMessageController(){}


    public synchronized static SdkMessageController getInstance(){
        if(sdkMessageController == null){
            sdkMessageController = new SdkMessageController();
        }
        return sdkMessageController;
    }
}

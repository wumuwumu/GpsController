package com.sciento.wumu.gpscontroller.DeviceSdk;

/**
 * Created by wumu on 17-7-16.
 */

public class DeviceListener {

    public void didDisConnected(Device device , int result){}

    public void didOnline(Device device , int  result){}

    public void didSetSubscribe(int errorcode , Device device ,int issubscribe){}
}

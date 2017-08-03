package com.sciento.wumu.gpscontroller.DeviceSdk;

import com.sciento.wumu.gpscontroller.MqttModule.CurrentLocation;

/**
 * Created by wumu on 17-7-16.
 */

public abstract class DeviceListener {

    public void didSubscribeState(Device device , int result){}

    public void didSubscribe(Device device , int  result){}

    public void didSetSubscribe(int errorcode , Device device ,int issubscribe){}

    public void didInfo(String info){};

   abstract public void didUpdateLocation(String deviceid,  CurrentLocation currentLocation);
}

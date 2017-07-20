package com.sciento.wumu.gpscontroller.DeviceSdk;

import java.util.List;

/**
 * Created by wumu on 17-7-17.
 */

public abstract class DeviceControllerListener {

    public DeviceControllerListener(){}

    public void DidGetAllDevice(int errorcode , List<Device> devices){}

    public void DidBindDevice(int errorcode){}

    public void DidUnbindDive(int errorcode){}

}

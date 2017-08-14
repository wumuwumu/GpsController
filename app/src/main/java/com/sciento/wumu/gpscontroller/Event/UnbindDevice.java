package com.sciento.wumu.gpscontroller.Event;

/**
 * Created by wumu on 17-8-12.
 */

public class UnbindDevice {

    private String deviceId;

    public UnbindDevice(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }


}

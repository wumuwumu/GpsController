package com.sciento.wumu.gpscontroller.Model;

/**
 * Created by wumu on 17-8-3.
 */

public class DeviceState {
    boolean power;

    String deviceId;

    public boolean isPower() {
        return power;
    }

    public void setPower(boolean power) {
        this.power = power;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}

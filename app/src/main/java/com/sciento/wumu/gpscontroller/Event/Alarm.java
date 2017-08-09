package com.sciento.wumu.gpscontroller.Event;

/**
 * Created by wumu on 17-8-7.
 */

public class Alarm {
    private String deviceId;
    private String deviceName;
    private boolean alarm;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public boolean isAlarm() {
        return alarm;
    }

    public void setAlarm(boolean alarm) {
        this.alarm = alarm;
    }
}

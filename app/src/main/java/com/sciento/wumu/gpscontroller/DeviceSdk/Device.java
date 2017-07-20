package com.sciento.wumu.gpscontroller.DeviceSdk;

/**
 * Created by wumu on 17-7-16.
 */

public class Device {
    protected DeviceListener deviceListener;

    private String macAdress;
    private String deviceId;
    private String deviceName;

    private boolean isOnline;
    private boolean subscribed;
    private boolean isDiable;

    public boolean getStatus(){
        return isOnline;
    }

    public String getMacAdress() {
        return macAdress;
    }

    public void setMacAdress(String macAdress) {
        this.macAdress = macAdress;
    }

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

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    public boolean isDiable() {
        return isDiable;
    }

    public void setDiable(boolean diable) {
        isDiable = diable;
    }
}

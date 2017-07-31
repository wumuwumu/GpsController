package com.sciento.wumu.gpscontroller.DeviceSdk;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wumu on 17-7-16.
 */

public class Device implements Parcelable {
    protected DeviceListener deviceListener;

    private String macAdress;
    private String deviceId;
    private String deviceName;

    private boolean isOnline;
    private boolean subscribed;
    private boolean isDiable;




    public static final Creator<Device> CREATOR = new Creator<Device>() {
        @Override
        public Device createFromParcel(Parcel in) {
            Device device = new Device();
            device.macAdress = in.readString();
            device.deviceId = in.readString();
            device.deviceName = in.readString();
            device.isOnline = in.readByte() != 0;
            device.subscribed = in.readByte() != 0;
            device.isDiable = in.readByte() != 0;
            return device;
        }

        @Override
        public Device[] newArray(int size) {
            return new Device[size];
        }
    };

    public boolean getStatus(){
        return isOnline;
    }


    public void setDeviceListener(DeviceListener deviceListener){
        this.deviceListener = deviceListener;
    }


    public void setScribe(boolean issubscribe){

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(macAdress);
        dest.writeString(deviceId);
        dest.writeString(deviceName);
        dest.writeByte((byte) (isOnline ? 1 : 0));
        dest.writeByte((byte) (subscribed ? 1 : 0));
        dest.writeByte((byte) (isDiable ? 1 : 0));
    }
}

package com.sciento.wumu.gpscontroller.Event;

/**
 * Created by wumu on 17-8-19.
 */

public class FenceAlarm {
    private boolean fencealarm;
    private String deviceid;

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public boolean isFencealarm() {
        return fencealarm;
    }

    public void setFencealarm(boolean fencealarm) {
        this.fencealarm = fencealarm;
    }
}

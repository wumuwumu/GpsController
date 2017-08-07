package com.sciento.wumu.gpscontroller.Event;

/**
 * Created by wumu on 17-8-7.
 */

public class DeviceDisconnect {
    String deviceId;
    String reason;
    long ts;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }
}

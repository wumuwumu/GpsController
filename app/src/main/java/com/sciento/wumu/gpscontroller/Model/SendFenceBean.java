package com.sciento.wumu.gpscontroller.Model;

/**
 * Created by wumu on 17-8-4.
 */

public class SendFenceBean {

    private String id;    // 设备id
    private Double longitude;    // 经度
    private Double latitude;     // 纬度
    private Double radius;       // 半径

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    private boolean state;       //是否启用
}

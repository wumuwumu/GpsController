package com.sciento.wumu.gpscontroller.Model.Model;

public class Fence {


    private String id;           //用设备id做这个位置的id

    private Double longitude;    // 经度

    private Double latitude;     // 纬度


    private boolean state;      //是否启用

    private Double radius;      //围栏半径

    private String updatedAt;


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

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public Double getRadius() {
        return radius;
    }

    public void setRadius(Double radius) {
        this.radius = radius;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }


}

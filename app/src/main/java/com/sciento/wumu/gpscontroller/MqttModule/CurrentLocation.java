package com.sciento.wumu.gpscontroller.MqttModule;

/**
 * Created by wumu on 17-7-8.
 */

public class CurrentLocation {

    private double latitude; //纬度
    private double longitude; //经度
    private  float accuracy;  //精度
    private double altitude; //海拔
    private  float speed; //速度
    private  float bearing; //方向角

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getBearing() {
        return bearing;
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
    }





}

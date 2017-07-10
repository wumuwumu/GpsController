package com.sciento.wumu.gpscontroller.MqttModule;

/**
 * Created by wumu on 17-7-8.
 */

public class CurrentLocation {

    private double Latitude; //纬度
    private double Longitude; //经度
    private  float Accuracy;  //精度
    private double Altitude; //海拔
    private  float Speed; //速度
    private  float Bearing; //方向角

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public float getAccuracy() {
        return Accuracy;
    }

    public void setAccuracy(float Accuracy) {
        this.Accuracy = Accuracy;
    }

    public double getAltitude() {
        return Altitude;
    }

    public void setAltitude(double altitude) {
        Altitude = altitude;
    }

    public float getSpeed() {
        return Speed;
    }

    public void setSpeed(float speed) {
        Speed = speed;
    }

    public float getBearing() {
        return Bearing;
    }

    public void setBearing(float bearing) {
        Bearing = bearing;
    }


}

package com.sciento.wumu.gpscontroller.MqttModule;

import android.location.Location;

import com.amap.api.location.AMapLocation;

import java.util.Currency;

/**
 * Created by wumu on 17-7-12.
 */

public class CurrentLocationController  {
    private CurrentLocation currentLocation = new CurrentLocation();

    public CurrentLocationController(Location location,String deviceid){
        currentLocation.setLatitude(location.getLatitude());
        currentLocation.setLongitude(location.getLongitude());
        currentLocation.setAccuracy(location.getAccuracy());
        currentLocation.setAltitude(location.getAltitude());
        currentLocation.setSpeed(location.getSpeed());
        currentLocation.setBearing(location.getBearing());
        currentLocation.setDeviceId(deviceid);
    }

    public CurrentLocationController(AMapLocation location,String deviceid){
        currentLocation.setLatitude(location.getLatitude());
        currentLocation.setLongitude(location.getLongitude());
        currentLocation.setAccuracy(location.getAccuracy());
        currentLocation.setAltitude(location.getAltitude());
        currentLocation.setSpeed(location.getSpeed());
        currentLocation.setBearing(location.getBearing());
        currentLocation.setDeviceId(deviceid);
    }

    CurrentLocation getCurrentLocation(){
        return currentLocation;
    }

}

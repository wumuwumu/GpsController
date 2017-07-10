package com.sciento.wumu.gpscontroller.MqttModule;

import android.location.Location;

import com.amap.api.location.AMapLocation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by wumu on 17-7-8.
 */

public class LocationToJson {





    public static String getJson(Location location){
        CurrentLocation currentLocation = new CurrentLocation();
        currentLocation.setLatitude(location.getLatitude());
        currentLocation.setLongitude(location.getLongitude());
        currentLocation.setAccuracy(location.getAccuracy());
        currentLocation.setAltitude(location.getAltitude());
        currentLocation.setSpeed(location.getSpeed());
        currentLocation.setBearing(location.getBearing());


        final GsonBuilder builder = new GsonBuilder();
        builder.setVersion(1.0);
        final Gson gson = builder.create();
        final String json = gson.toJson(currentLocation);

        return json;

    }

    public static String getJson(AMapLocation location){
        CurrentLocation currentLocation = new CurrentLocation();
        currentLocation.setLatitude(location.getLatitude());
        currentLocation.setLongitude(location.getLongitude());
        currentLocation.setAccuracy(location.getAccuracy());
        currentLocation.setAltitude(location.getAltitude());
        currentLocation.setSpeed(location.getSpeed());
        currentLocation.setBearing(location.getBearing());


        final GsonBuilder builder = new GsonBuilder();
        builder.setVersion(1.0);
        final Gson gson = builder.create();
        final String json = gson.toJson(currentLocation);

        return json;

    }


}

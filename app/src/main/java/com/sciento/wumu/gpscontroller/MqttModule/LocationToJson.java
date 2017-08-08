package com.sciento.wumu.gpscontroller.MqttModule;

import android.location.Location;

import com.amap.api.location.AMapLocation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sciento.wumu.gpscontroller.Model.DeviceState;
import com.sciento.wumu.gpscontroller.Model.JsonDevice;
import com.sciento.wumu.gpscontroller.Model.SendFenceBean;

import java.util.List;

/**
 * Created by wumu on 17-7-8.
 */

public class LocationToJson {

    private static  GsonBuilder builder;
    private static Gson gson;

    static {
         builder = new GsonBuilder();
        builder.setVersion(1.0);
        gson = builder.create();
    }



    public static String getJson(Location location){
        CurrentLocationController currentLocation = new CurrentLocationController(location,"wumu");

        final String json = gson.toJson(currentLocation.getCurrentLocation());

        return json;

    }

    public static String getJson(AMapLocation location){
        CurrentLocationController currentLocation = new CurrentLocationController(location,"wumu");


        final String json = gson.toJson(currentLocation);

        return json;

    }

    public static <T> T getPojo(String jsonData, Class<T> type) {

                 T result = gson.fromJson(jsonData, type);
                 return result;
             }


    public static String getStateJson(DeviceState deviceState){
        final String json = gson.toJson(deviceState);

        return json;
    }

    public static String getFenceJson(SendFenceBean sendFence){
        final String json = gson.toJson(sendFence);

        return json;
    }

    public static  List<JsonDevice> jsonToList(String json) {
        List<JsonDevice> list = gson.fromJson(json, new TypeToken<List<JsonDevice>>(){}.getType());
        return list;
    }



}

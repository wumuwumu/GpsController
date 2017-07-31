package com.sciento.wumu.gpscontroller.ConfigModule;

/**
 * Created by wumu on 17-7-8.
 */


public class Config {

    //mqtt
    public static final String MQTTSERVER ="tcp://39.108.8.161:1883";
    public static final int MQTTPORT =1883;
    public static final String TOPICSEND ="topic://wumu/up";
    public static final String TOPICGET ="topic://wumu/down";
    public static final String CLIENTID =  "wumu12";

    //http
    public static final String HTTPSERVER =  "http://192.168.0.121:8080";


    //db
    public static final String REALMDB= "gps.realm";
    public static final int dbVersion = 1;



}

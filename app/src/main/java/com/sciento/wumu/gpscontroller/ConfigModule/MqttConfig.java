package com.sciento.wumu.gpscontroller.ConfigModule;

/**
 * Created by wumu on 17-7-8.
 */


public class MqttConfig {
    public static final String MQTTSERVER ="tcp://192.168.31.230:1883";
    public static final int MQTTPORT =1883;
    public static final String TOPICSEND ="topic://wumu/up";
    public static final String TOPICGET ="topic://wumu/down";
    public static final String CLIENTID =  "wumu";

}

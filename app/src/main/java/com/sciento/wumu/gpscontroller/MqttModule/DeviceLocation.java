package com.sciento.wumu.gpscontroller.MqttModule;

import android.app.Application;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.sciento.wumu.gpscontroller.CommonModule.AppContext;
import com.sciento.wumu.gpscontroller.ConfigModule.Config;
import com.sciento.wumu.gpscontroller.ConfigModule.UserState;
import com.sciento.wumu.gpscontroller.Event.Alarm;
import com.sciento.wumu.gpscontroller.Event.DeviceConnected;
import com.sciento.wumu.gpscontroller.Event.DeviceDisconnect;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;

/**
 * Created by wumu on 17-7-8.
 */

public class DeviceLocation {

    private static DeviceLocation deviceLocation = new DeviceLocation();
    private static boolean isConnected = false;
    private final String TAG ="MqttConnect";
    private final int MAG_DIS_TO_CONN = 903;
    private MqttAndroidClient mqttAndroidClient;
    private IMqttToken token;
    private IMqttActionListener iMqttActionListener = new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            // We are connected
            isConnected = true;
            Log.d(TAG, "onSuccess");
            Toast.makeText(AppContext.getContext(),"suc",Toast.LENGTH_SHORT).show();
//                    try {
//                        DeviceLocation.getInstance().getMqttAndroidClient().subscribe(Config.TOPICSEND, 0);
//                    } catch (MqttException e) {
//                        e.printStackTrace();
//                    }

        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            // Something went wrong e.g. connection timeout or firewall problems
            isConnected = false;
            Log.d(TAG, "onFailure");
            Toast.makeText(AppContext.getContext(),"fail",Toast.LENGTH_SHORT).show();
            connectHandler.sendEmptyMessage(MAG_DIS_TO_CONN);

        }
    };
    private MqttCallback mqttCallback = new MqttCallback() {
        @Override
        public void connectionLost(Throwable cause) {

        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            EventBus.getDefault().post(message);
            if(topic.matches("^topic://.*/.*/.*$")) {
                String[] topicUpArray = topic.split("/");
                if (topicUpArray.length == 5) {
                    if (topicUpArray[3].equals("up")) {
                        if (topicUpArray[4].equals("location")) {
                            CurrentLocation currentLocation = LocationToJson
                                    .getPojo(message.toString(), CurrentLocation.class);
                            EventBus.getDefault().post(currentLocation);
                        } else if (topicUpArray[4].equals("alarm")) {
                            Alarm alarm = LocationToJson.getPojo(message.toString(), Alarm.class);
                            EventBus.getDefault().post(alarm);
                        }

                    }
                }

            }else if(topic.matches(".*/connected")) {
                String[] topicConArray = topic.split("/");
                if(topicConArray.length == 6){
                    DeviceConnected deviceConnected = LocationToJson
                            .getPojo(message.toString(), DeviceConnected.class);
                    EventBus.getDefault().post(deviceConnected);
                }
            }else  if(topic.matches(".*/disconnected")){
                String[] topicConArray = topic.split("/");
                if(topicConArray.length == 6){
                    DeviceDisconnect deviceDisconnect = LocationToJson
                            .getPojo(message.toString(), DeviceDisconnect.class);
                    EventBus.getDefault().post(deviceDisconnect);
                }
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {

        }
    };
    Handler connectHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MAG_DIS_TO_CONN:
                    connect();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private DeviceLocation() {
        mqttAndroidClient = new MqttAndroidClient(AppContext.getContext(), Config.MQTTSERVER,
                UserState.username);
    }

    public static synchronized DeviceLocation getInstance() {
        return deviceLocation;
    }

    public void connect()
    {

        try {
            token = mqttAndroidClient.connect();
        } catch (MqttException e) {
            e.printStackTrace();
        }


        token.setActionCallback(iMqttActionListener);

        mqttAndroidClient.setCallback(mqttCallback);

    }

    public void sendLocation(String topic , String strmessage){
        byte[] encodedPayload = null;
        try {
            encodedPayload = strmessage.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            if(isConnected == true){
                mqttAndroidClient.publish(topic, message);
            }else {

            }

        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }

    }

    public void sendRetainMessage(String topic ,String strmessage ){
        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = strmessage.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            message.setRetained(true);
            mqttAndroidClient.publish(topic, message);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }

    public MqttAndroidClient getMqttAndroidClient(){
        return mqttAndroidClient;
    }


}

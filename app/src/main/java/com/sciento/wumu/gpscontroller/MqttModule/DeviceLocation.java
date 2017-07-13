package com.sciento.wumu.gpscontroller.MqttModule;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.sciento.wumu.gpscontroller.CommonModule.AppContext;
import com.sciento.wumu.gpscontroller.ConfigModule.Config;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

/**
 * Created by wumu on 17-7-8.
 */

public class DeviceLocation {

    private static DeviceLocation deviceLocation = new DeviceLocation();
    private static boolean isConnected = false;
    private MqttAndroidClient mqttAndroidClient;
    private IMqttToken token;

    private final String TAG ="MqttConnect";

    private DeviceLocation(){
       mqttAndroidClient =  new MqttAndroidClient(AppContext.getContext(), Config.MQTTSERVER,
                Config.CLIENTID);
    }

    public static synchronized DeviceLocation getInstance()
    {
        return deviceLocation;
    }

    public void connect()
    {

        try {
            token = mqttAndroidClient.connect();
        } catch (MqttException e) {
            e.printStackTrace();
        }


        token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    isConnected = true;
                    Log.d(TAG, "onSuccess");
                    Toast.makeText(AppContext.getContext(),"suc",Toast.LENGTH_SHORT).show();
                    try {
                        DeviceLocation.getInstance().getMqttAndroidClient().subscribe(Config.TOPICSEND, 0);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    isConnected = false;
                    Log.d(TAG, "onFailure");
                    Toast.makeText(AppContext.getContext(),"fail",Toast.LENGTH_SHORT).show();

                }
            });



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

    public MqttAndroidClient getMqttAndroidClient(){
        return mqttAndroidClient;
    }


}

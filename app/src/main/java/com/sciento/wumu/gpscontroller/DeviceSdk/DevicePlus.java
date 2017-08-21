package com.sciento.wumu.gpscontroller.DeviceSdk;

import android.bluetooth.BluetoothClass;

import com.sciento.wumu.gpscontroller.ConfigModule.Config;
import com.sciento.wumu.gpscontroller.Model.JsonDevice;
import com.sciento.wumu.gpscontroller.MqttModule.CurrentLocation;
import com.sciento.wumu.gpscontroller.MqttModule.DeviceLocation;
import com.sciento.wumu.gpscontroller.MqttModule.LocationToJson;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by wumu on 17-7-16.
 */

public class DevicePlus {
    protected DeviceListener deviceListener;
    IMqttActionListener iMqttActionListener = new IMqttActionListener(){

        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            deviceListener.didSubscribeState(null,111);
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            deviceListener.didSubscribeState(null,0);
        }
    };
    MqttCallback mqttCallback = new MqttCallback() {
        @Override
        public void connectionLost(Throwable cause) {
            deviceListener.didInfo("error");
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {

//            CurrentLocation currentLocation = LocationToJson.getPojo(message.toString(),
//                    CurrentLocation.class);
//            deviceListener.didUpdateLocation(getDeviceId(),currentLocation);
//            DeciceBean deciceBean = new DeciceBean();
//            deciceBean.setDeviceId("12345");
//            EventBus.getDefault().post(deciceBean);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {

        }
    };
    private boolean subscribed = false;
    private JsonDevice jsonDevice;

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    public JsonDevice getJsonDevice() {
        return jsonDevice;
    }

    public void setJsonDevice(JsonDevice jsonDevice) {
        this.jsonDevice = jsonDevice;
    }

    public void setDeviceListener(DeviceListener deviceListener){
        this.deviceListener = deviceListener;
    }





    public void setSubscribe(boolean ensubscribe){
        if(ensubscribe){
            if(!isSubscribed()){
                try {
                    IMqttToken subToken = DeviceLocation.getInstance().getMqttAndroidClient()
                            .subscribe("topic://"+jsonDevice.getId()+"/up/location", 0);
                    IMqttToken subTokenAlarm = DeviceLocation.getInstance().getMqttAndroidClient()
                            .subscribe("topic://" + jsonDevice.getId() + "/up/alarm", 0);
                    IMqttToken subTokenConn = DeviceLocation.getInstance().getMqttAndroidClient()
                            .subscribe("$SYS/brokers/"+Config.MQTTNODE+
                                    "/clients/"+jsonDevice.getId()+"/connected", 0);
                    IMqttToken subTokenDis = DeviceLocation.getInstance().getMqttAndroidClient()
                            .subscribe("$SYS/brokers/"+Config.MQTTNODE+
                                    "/clients/" + jsonDevice.getId() + "/disconnected", 0);
//                    DeviceLocation.getInstance().getMqttAndroidClient()
//                            .subscribe(Config.TOPICSEND, 0);
//                    subToken.setActionCallback(iMqttActionListener);
                    //deviceListener.didSubscribeState(null,100);

                } catch (MqttException e) {
                    e.printStackTrace();
                }
//                DeviceLocation.getInstance().getMqttAndroidClient().setCallback(mqttCallback);
                setSubscribed(true);
            }
        }else{
            if(isSubscribed()){
                try {
                    IMqttToken subToken = DeviceLocation.getInstance().getMqttAndroidClient()
                            .unsubscribe("topic://" + jsonDevice.getId() + "/up/location");
                    IMqttToken subTokenConn = DeviceLocation.getInstance().getMqttAndroidClient()
                            .unsubscribe("$SYS/brokers/" + Config.MQTTNODE +
                                    "/clients/" + jsonDevice.getId() + "/connected");
                    IMqttToken subTokenDis = DeviceLocation.getInstance().getMqttAndroidClient()
                            .unsubscribe("$SYS/brokers/" + Config.MQTTNODE +
                                    "/clients/" + jsonDevice.getId() + "/disconnected");
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                setSubscribed(false);
            }

        }
    }






}

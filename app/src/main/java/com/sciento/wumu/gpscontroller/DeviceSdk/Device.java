package com.sciento.wumu.gpscontroller.DeviceSdk;

import com.sciento.wumu.gpscontroller.ConfigModule.Config;
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

public class Device  {
    protected DeviceListener deviceListener;

    private String macAdress = "0000000000";
    private String deviceId = "0000000000";
    private String deviceName = "0000000000";

    private boolean isOnline = false;
    private boolean subscribed = false;
    private boolean isDiable = false;




//    public static final Creator<Device> CREATOR = new Creator<Device>() {
//        @Override
//        public Device createFromParcel(Parcel in) {
//            Device device = new Device();
//            device.macAdress = in.readString();
//            device.deviceId = in.readString();
//            device.deviceName = in.readString();
//            device.isOnline = in.readByte() != 0;
//            device.subscribed = in.readByte() != 0;
//            device.isDiable = in.readByte() != 0;
//            return device;
//        }
//
//        @Override
//        public Device[] newArray(int size) {
//            return new Device[size];
//        }
//    };

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

    public boolean getStatus(){
        return isOnline;
    }


    public void setDeviceListener(DeviceListener deviceListener){
        this.deviceListener = deviceListener;
    }





    public void setSubscribe(boolean ensubscribe){
        if(ensubscribe){
            if(!isSubscribed()){
                try {
                    IMqttToken subToken = DeviceLocation.getInstance().getMqttAndroidClient()
                            .subscribe(Config.TOPICSEND, 0);
//                    DeviceLocation.getInstance().getMqttAndroidClient()
//                            .subscribe(Config.TOPICSEND, 0);
                    subToken.setActionCallback(iMqttActionListener);
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
                            .unsubscribe("topic://"+deviceId+"/up");
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                setSubscribed(false);
            }

        }
    }





    public String getMacAdress() {
        return macAdress;
    }

    public void setMacAdress(String macAdress) {
        this.macAdress = macAdress;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    public boolean isDiable() {
        return isDiable;
    }

    public void setDiable(boolean diable) {
        isDiable = diable;
    }


//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(macAdress);
//        dest.writeString(deviceId);
//        dest.writeString(deviceName);
//        dest.writeByte((byte) (isOnline ? 1 : 0));
//        dest.writeByte((byte) (subscribed ? 1 : 0));
//        dest.writeByte((byte) (isDiable ? 1 : 0));
//    }
}

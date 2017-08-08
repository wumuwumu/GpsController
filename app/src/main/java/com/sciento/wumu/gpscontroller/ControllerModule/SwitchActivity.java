package com.sciento.wumu.gpscontroller.ControllerModule;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.sciento.wumu.gpscontroller.DeviceModule.DeviceBaseFragment;
import com.sciento.wumu.gpscontroller.DeviceSdk.DevicePlus;
import com.sciento.wumu.gpscontroller.Model.DeviceState;
import com.sciento.wumu.gpscontroller.MqttModule.DeviceLocation;
import com.sciento.wumu.gpscontroller.MqttModule.LocationToJson;
import com.sciento.wumu.gpscontroller.R;

import java.util.ListIterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SwitchActivity extends AppCompatActivity {

    @BindView(R.id.btn_switch_device)
    Button btnSwitchDevice;
    boolean enswitch = false;
    String deviceId;

     private final  int MSG_UPDATE_BTN = 123;

    Handler switchHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_UPDATE_BTN:
                    ListIterator<DevicePlus> deviceListIterator = DeviceBaseFragment.deviceslist.listIterator();
                    while (deviceListIterator.hasNext()){
                        if(deviceListIterator.next().getJsonDevice().getId().equals(deviceId)){
                            enswitch = deviceListIterator.next().getJsonDevice().isPower();
                            if(enswitch){
                                btnSwitchDevice.setText(getString(R.string.str_open));
                            }else {
                                btnSwitchDevice.setText(getString(R.string.str_close));
                            }
                        }
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch);
        ButterKnife.bind(this);
        deviceId = getIntent().getExtras().getString("deviceid");
        switchHandler.sendEmptyMessage(MSG_UPDATE_BTN);

    }

    @OnClick(R.id.btn_switch_device)
    public void onViewClicked() {
        if(enswitch){
            btnSwitchDevice.setText(getString(R.string.str_close));
            enswitch =false;

        }else {
            btnSwitchDevice.setText(getString(R.string.str_open));
            enswitch =true;
        }
        DeviceState deviceState = new DeviceState();
        deviceState.setPower(enswitch);
        deviceState.setDeviceId(deviceId);
        DeviceLocation.getInstance().sendRetainMessage("topic://"+deviceId+"/down/power",
                LocationToJson.getStateJson(deviceState));
    }
}

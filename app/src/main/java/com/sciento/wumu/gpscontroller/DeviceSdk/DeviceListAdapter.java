package com.sciento.wumu.gpscontroller.DeviceSdk;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sciento.wumu.gpscontroller.R;

import java.util.List;

/**
 * Created by wumu on 17-7-20.
 */

public class DeviceListAdapter extends BaseAdapter {


    Handler handler = new Handler();
    protected static final int MSG_UNBOUND = 14;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    Context context;
    List<Device> deviceList;

    public DeviceListAdapter(Context context, List<Device> deviceList) {
        super();
        this.context = context;
        this.deviceList = deviceList;
    }

    @Override
    public int getCount() {
        return deviceList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        HoldStatus holdStatus;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.item_deivce_list,null);
            holdStatus = new HoldStatus(view);
            view.setTag(holdStatus);
        }else{
            holdStatus = (HoldStatus) view.getTag();
        }

        final Device device = deviceList.get(position);

        String online = context.getString(R.string.str_online);
        String offline = context.getString(R.string.str_offine);
        String deviceName = device.getDeviceName();
        String deviceMac = device.getMacAdress();

        if(device.getStatus() ==  true){
            holdStatus.getDeviceName().setText(deviceName);
            holdStatus.getvDeviceId().setText(deviceMac);
            holdStatus.getDeviceStatus().setError(online);
        }else{
            holdStatus.getDeviceName().setText(deviceName);
            holdStatus.getvDeviceId().setText(deviceMac);
            holdStatus.getDeviceStatus().setError(offline);
            holdStatus.getGoControl().setVisibility(View.GONE);
        }

        holdStatus.getRightUnbind().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = MSG_UNBOUND;
                message.obj = device.getDeviceId().toString();
                handler.sendMessage(message);
            }
        });

        return view;
    }




}


class HoldStatus{
    View view;

    public HoldStatus(View view){
        this.view = view;
    }

    private  TextView tvDeviceName;
    private  TextView tvDeviceId;
    private  TextView tvDeviceStatus;

    private Button btnGoControl;

    private LinearLayout llRightUnbind;

    public TextView getvDeviceId(){
        if(null == tvDeviceId){
            tvDeviceId = (TextView)view.findViewById(R.id.tv_deivce_id);
        }
        return tvDeviceId;
    }

    public TextView getDeviceName(){
        if(null == tvDeviceName){
            tvDeviceName = (TextView)view.findViewById(R.id.tv_deivce_name);
        }
        return tvDeviceName;
    }

    public TextView getDeviceStatus(){
        if(null == tvDeviceStatus){
            tvDeviceStatus = (TextView)view.findViewById(R.id.tv_device_staus);
        }
        return  tvDeviceStatus;
    }

    public Button getGoControl(){
        if(null == btnGoControl){
            btnGoControl = (Button)view.findViewById(R.id.btn_go_control);
        }
        return btnGoControl;
    }

    public LinearLayout getRightUnbind(){
        if(null == llRightUnbind){
            llRightUnbind = (LinearLayout)view.findViewById(R.id.ll_right_unbind_device);
        }
        return llRightUnbind;
    }




}
package com.sciento.wumu.gpscontroller.DeviceSdk;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sciento.wumu.gpscontroller.R;

import java.util.List;

/**
 * Created by wumu on 17-7-20.
 */

public class DeviceListAdapter extends BaseAdapter {


    Handler handler = new Handler();
    protected static final int MSG_UNBOUND = 14;
    private final int MSG_CONTTROLLER = 12;

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    Context context;
    List<DevicePlus> deviceList;

    public DeviceListAdapter(Context context, List<DevicePlus> deviceList) {
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

        final DevicePlus device = deviceList.get(position);

        String online = context.getString(R.string.str_online);
        String offline = context.getString(R.string.str_offine);
        String deviceName = device.getJsonDevice().getName();
        String deviceid = device.getJsonDevice().getId();

        if(device.getJsonDevice().isOnline() ==  true){
            holdStatus.getDeviceName().setText(deviceName);
            holdStatus.getvDeviceId().setText(deviceid);
            holdStatus.getDeviceStatus().setText(online);
        }else{
            holdStatus.getDeviceName().setText(deviceName);
            holdStatus.getvDeviceId().setText(deviceid);
            holdStatus.getDeviceStatus().setText(offline);
            holdStatus.getGoControl().setVisibility(View.GONE);
        }

        holdStatus.getRightUnbind().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = MSG_UNBOUND;
                message.obj = device.getJsonDevice().getId().toString();
                handler.sendMessage(message);
            }
        });

        holdStatus.getGoControl().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = MSG_CONTTROLLER;
                message.obj = device.getJsonDevice().getId().toString();
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

    private RelativeLayout llRightUnbind;

    public TextView getvDeviceId(){
        if(null == tvDeviceId){
            tvDeviceId = (TextView)view.findViewById(R.id.tv_deivce_id);
        }
        return tvDeviceId;
    }

    public View getView(){
        return view;
    }

    public TextView getDeviceName(){
        if(null == tvDeviceName){
            tvDeviceName = (TextView)view.findViewById(R.id.tv_deivce_name);
        }
        return tvDeviceName;
    }

    public TextView getDeviceStatus(){
        if(null == tvDeviceStatus){
            tvDeviceStatus = (TextView)view.findViewById(R.id.tv_is_online);
        }
        return  tvDeviceStatus;
    }

    public Button getGoControl(){
        if(null == btnGoControl){
            btnGoControl = (Button)view.findViewById(R.id.btn_go_control);
        }
        return btnGoControl;
    }

    public RelativeLayout getRightUnbind(){
        if(null == llRightUnbind){
            llRightUnbind = (RelativeLayout)view.findViewById(R.id.ll_right_unbind_device);
        }
        return llRightUnbind;
    }




}
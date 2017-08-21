package com.sciento.wumu.gpscontroller.DeviceSdk;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sciento.wumu.gpscontroller.CommonModule.AppContext;
import com.sciento.wumu.gpscontroller.ConfigModule.Config;
import com.sciento.wumu.gpscontroller.ConfigModule.UserState;
import com.sciento.wumu.gpscontroller.Model.FenceInfo;
import com.sciento.wumu.gpscontroller.Model.SendFenceBean;
import com.sciento.wumu.gpscontroller.Model.JsonDevice;
import com.sciento.wumu.gpscontroller.MqttModule.DeviceLocation;
import com.sciento.wumu.gpscontroller.MqttModule.LocationToJson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wumu on 17-7-17.
 */

public class SdkEventController {
    private static final SdkEventController sdkEventController = new SdkEventController();
    private static DeviceControllerListener deviceControllerListener;
    private static List<DevicePlus> deviceArrayList =  new ArrayList<>();

    //msg
    private final int MSG_GET_ALL_DEVICE = 503;
    private final int MSG_GET_BIND_ERROR = 504;
    private final int MSG_GET_BIND_DEVICE = 505;
    private final int MSG_GET_UNBind_DEVICE = 506;
    private final int MSG_GET_UNBIND_ERROR = 507;

    Handler handler = new Handler(Looper.getMainLooper()){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_GET_BIND_DEVICE:

                    if((int)msg.obj == 1){
                        onDidBindDevice(ErrorCode.CODE_SUCCESS);


                    }else if((int)msg.obj == 0){
                        onDidBindDevice(ErrorCode.CODE_BIND_FAIL);
                    }
                    break;
                case MSG_GET_BIND_ERROR:
                    onDidBindDevice(ErrorCode.CODE_SERVER_ERROR);
                    break;
                case MSG_GET_UNBind_DEVICE:
                    if((int)msg.obj == 1){
                        onDidBindDevice(ErrorCode.CODE_SUCCESS);
                    }else if((int)msg.obj == 0){
                        onDidBindDevice(ErrorCode.CODE_BIND_FAIL);
                    }
                    break;
                case MSG_GET_UNBIND_ERROR:

                    break;
            }
        }
    };





    public synchronized static SdkEventController getInstance(){
        return sdkEventController;
    }

    public void setDeviceControllerListener(DeviceControllerListener deviceControllerListener){
        this.deviceControllerListener = deviceControllerListener;
    }

    public void bindDevice(final String userphone, final String token , final String deviceId){
        String url =Config.HTTPSERVER+"/api/bind";

        Map<String, String>map = new HashMap<>();
        map.put("userName",userphone);
        map.put("deviceId",deviceId);
        JSONObject jsonObject = new JSONObject(map);
        // 参数：[请求方式][请求链接][请求参数][成功回调][失败回调]
        JsonObjectRequest mStringRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int status  =-2;
                        try {
                            status = response.getInt("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(status ==1){
                            getAllDeviceList(userphone, token);
                        }

                        Message message = new Message();
                        message.what = MSG_GET_BIND_DEVICE;
                        message.obj = status;
                        handler.sendMessage(message);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handler.sendEmptyMessage(MSG_GET_BIND_ERROR);
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Cookie" , UserState.referer );
                return params;
            }
        };

        mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                20 * 1000, 3, 1.0f));
        mStringRequest.setTag("bind");// 设置标签
        AppContext.getRequestQueue().add(mStringRequest);// 将请求添加进队列
//
//
//        ListIterator<DevicePlus> deviceListIterator = deviceArrayList.listIterator();
//        boolean enGo = true;
//        while (deviceListIterator.hasNext()){
//            if(deviceListIterator.next().getDeviceId().equals(deviceId)){
//                onDidBindDevice(ErrorCode.CODE_BIND_FAIL);
//                enGo=false;
//                break;
//            }
//        }
//
//        if(enGo){
//            DevicePlus device = new DevicePlus();
//            device.setDeviceId(deviceId);
//            deviceArrayList.add(device);
//            onDidGetAllDevice(ErrorCode.CODE_SUCCESS,deviceArrayList);
//            onDidBindDevice(ErrorCode.CODE_SUCCESS);
//        }

        //send server
//        getAllDeviceList(userphone, token);

    }


    public void unBindDevice(final String userphone, final String token , String deviceid){
        String url =Config.HTTPSERVER+"/api/unBind?deviceId="+deviceid;

        Map<String, String>map = new HashMap<>();
        map.put("deviceId",deviceid);
        JSONObject jsonObject = new JSONObject(map);
        // 参数：[请求方式][请求链接][请求参数][成功回调][失败回调]
        JsonObjectRequest mStringRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int status  =-2;
                        try {
                            status = response.getInt("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(status ==1){
                            getAllDeviceList(userphone, token);
                        }
                        Message message = new Message();
                        message.what = MSG_GET_UNBind_DEVICE;
                        message.obj = status;
                        handler.sendMessage(message);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handler.sendEmptyMessage(MSG_GET_UNBIND_ERROR);
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Cookie" , UserState.referer );
                return params;
            }
        };

        mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                20 * 1000, 1, 1.0f));
        mStringRequest.setTag("bind");// 设置标签
        AppContext.getRequestQueue().add(mStringRequest);// 将请求添加进队列

//        ListIterator<DevicePlus> deviceIterable = deviceArrayList.listIterator();
//        boolean enGo = true;
//        while(deviceIterable.hasNext()){
//            if((deviceIterable.next().getDeviceId()).equals(deviceid)){
//                deviceIterable.remove();
//                enGo = false;
//
//            }
//        }
//
//        if (!enGo){
//            onDidUnbindDevice(ErrorCode.CODE_SUCCESS);
//            onDidGetAllDevice(ErrorCode.CODE_SUCCESS,deviceArrayList);
//        }else {
//            onDidUnbindDevice(ErrorCode.CODE_UNBIND_FAIL);
//        }
    }


    public void getAllDeviceList(String userphone,String token ){
        String url =Config.HTTPSERVER+"/api/searchDeviceUser?uId="+UserState.uId;

        Map<String, String>map = new HashMap<>();
        map.put("Accept","*/*");
        JSONObject jsonObject = new JSONObject(map);
        // 参数：[请求方式][请求链接][请求参数][成功回调][失败回调]
        JsonObjectRequest mStringRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int status  =-2;
                        JSONArray jsonArray = null;
                        try {
                            status = response.getInt("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(status == 1){
                            try {
                                jsonArray=  response.getJSONObject("result").getJSONArray("pageInfo");
//                                List<String> list = new ArrayList<String>();
//                                for (int i=0; i<jsonArray.length(); i++) {
//                                    list.add( jsonArray.getString(i) );
//                                }
//                                String stringArray = list.toString();
                                List<JsonDevice> jsonDeviceList = LocationToJson.
                                        jsonToList(jsonArray.toString());
                                List<DevicePlus> devicePlusList = new ArrayList<>();
                                if( jsonDeviceList.isEmpty()){
                                    onDidGetAllDevice(ErrorCode.CODE_SUCCESS,devicePlusList);
                                }else {
                                    for (int i = 0; i < jsonDeviceList.size(); i++) {
                                        DevicePlus devicePlus = new DevicePlus();
                                        devicePlus.setJsonDevice(jsonDeviceList.get(i));
                                        devicePlusList.add(devicePlus);
                                    }
                                    onDidGetAllDevice(ErrorCode.CODE_SUCCESS, devicePlusList);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }else if(status ==0){
                            onDidGetAllDevice(ErrorCode.CODE_GET_ALL_FAIL,null);

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        onDidGetAllDevice(ErrorCode.CODE_SERVER_ERROR,null);
                        onDidRequestError(error.toString());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Cookie" , UserState.referer );
                return params;
            }
        };

        mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                20 * 1000, 5, 1.0f));
        mStringRequest.setTag("getall");// 设置标签
        AppContext.getRequestQueue().add(mStringRequest);// 将请求添加进队列


//        onDidGetAllDevice(ErrorCode.CODE_SUCCESS,deviceArrayList);
    }

    public void updataDevice(String deivceid,boolean isonline){
//        ListIterator<DevicePlus> deviceIterable = deviceArrayList.listIterator();
//        boolean enGo = true;
//        while(deviceIterable.hasNext()){
//            if((deviceIterable.next().getDeviceId()).equals(deivceid)){
//                deviceIterable.next().setOnline(isonline);
//                onDidUpdataDevice(ErrorCode.CODE_SUCCESS);
//                enGo = false;
//
//            }
//        }
//        if(enGo){
//            onDidUpdataDevice(ErrorCode.CODE_UPDATE_FAIL);
//        }
    }



    //listener
    protected void onDidGetAllDevice(int errorCode,List<DevicePlus> devices){
        deviceControllerListener.DidGetAllDevice(errorCode,devices);
    }

    protected void onDidBindDevice(int errorcode ){
        deviceControllerListener.DidBindDevice(errorcode);
    }

    protected void onDidUnbindDevice(int errorcode ){
        deviceControllerListener.DidUnbindDevice(errorcode);
    }

    protected void onDidUpdataDevice(int errorcode ){
        deviceControllerListener.DidUpdateDevice(errorcode);
    }

    protected void onDidRequestError(String errormessage) {
        deviceControllerListener.DidRequestError(errormessage);
    }


    //Fence
    public void getfencestate(String deviceid) {
        String url = Config.HTTPSERVER + "/api/getRail?deviceId=" + deviceid;
        Map<String, String>map = new HashMap<>();
        map.put("deviceid",deviceid);
        JSONObject jsonObject = new JSONObject(map);
        // 参数：[请求方式][请求链接][请求参数][成功回调][失败回调]
        JsonObjectRequest mStringRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        FenceInfo fenceInfo = LocationToJson.getPojo(response.toString(),FenceInfo.class);
                        EventBus.getDefault().post(fenceInfo);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Cookie" , UserState.referer );
                return params;
            }
        };

        mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                20 * 1000, 3, 1.0f));
        mStringRequest.setTag("getfence");// 设置标签
        AppContext.getRequestQueue().add(mStringRequest);// 将请求添加进队列

    }


    public void sendFenceInfo(final SendFenceBean sendFence, final FenceListener fenceListener) {
        String url =   Config.HTTPSERVER+"/api/setRail";

        final String strjson = LocationToJson.getFenceJson(sendFence);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(strjson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 参数：[请求方式][请求链接][请求参数][成功回调][失败回调]
        JsonObjectRequest mStringRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int status = -2;
                        try {
                            status = response.getInt("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        fenceListener.didSendFence(status);
                        if (status == 1) {
                            DeviceLocation.getInstance().sendRetainMessage(
                                    "topic://" + sendFence.getId() + "/down/fence", strjson);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        fenceListener.didSendFence(-1);
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Cookie" , UserState.referer );
                return params;
            }
        };

        mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                20 * 1000, 1, 1.0f));
        mStringRequest.setTag("12");// 设置标签
        AppContext.getRequestQueue().add(mStringRequest);// 将请求添加进队列
    }
}

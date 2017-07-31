package com.sciento.wumu.gpscontroller.UserModule;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sciento.wumu.gpscontroller.CommonModule.AppContext;
import com.sciento.wumu.gpscontroller.ConfigModule.Config;
import com.sciento.wumu.gpscontroller.ConfigModule.StateCode;
import com.sciento.wumu.gpscontroller.R;
import com.sciento.wumu.gpscontroller.Utils.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by wumu on 17-7-31.
 */

public class UserNetworkConn {
    private static UserNetworkConn userNetworkConn ;
    private UserNetworkConn() {
    }

    public static UserNetworkConn getInstance() {
        if(userNetworkConn == null)
        {
            synchronized (UserNetworkConn.class) {
                userNetworkConn = new UserNetworkConn();
            }
        }

        return userNetworkConn;
    }

    private Map<String, String> params ;
    private JSONObject jsonObject ;
    private JsonObjectRequest jsonObjectRequest;

    public synchronized void requestServer(int method, String url, Map<String, String> params, final Handler handler){
        jsonObject = new JSONObject(params);// 将 Map 转为 JsonObject 的参数

        // 参数：[请求方式][请求链接][请求参数][成功回调][失败回调]
        jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                Config.HTTPSERVER+"/api/login",
                jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        int code = 0;
                        try {
                            code = response.getInt("status");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (code == StateCode.USER_SUCCESS) {
                            Message msg = Message.obtain();
                            msg.what = StateCode.USER_SUCCESS;
                            handler.sendMessage(msg);
                        } else {
                            Message msg = Message.obtain();
                            msg.what = StateCode.USER_SIGN_NOMATCH;
                            handler.sendMessage(msg);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

//                        if (!NetworkUtils.isConnected(UserLoginActivity.this)) {
//                            Toast.makeText(UserLoginActivity.this, R.string.error_network_dis,
//                                    Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(UserLoginActivity.this, R.string.error_link_server,
//                                    Toast.LENGTH_SHORT).show();
//                        }

                        Message msg = Message.obtain();
                        msg.what = StateCode.REQUEST_ERROR;
                        handler.sendMessage(msg);

                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                20 * 1000, 1, 1.0f));
        jsonObjectRequest.setTag("doJsonPost");// 设置标签
        AppContext.getRequestQueue().add(jsonObjectRequest);// 将请求添加进队列
    }








}

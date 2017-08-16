package com.sciento.wumu.gpscontroller.UserModule;

import android.os.Handler;
import android.os.Message;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sciento.wumu.gpscontroller.CommonModule.AppContext;
import com.sciento.wumu.gpscontroller.ConfigModule.Config;
import com.sciento.wumu.gpscontroller.ConfigModule.UserStateCode;
import com.sciento.wumu.gpscontroller.ConfigModule.UserState;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wumu on 17-7-31.
 */

public class UserNetworkConn {
    private static UserNetworkConn userNetworkConn ;
    private Map<String, String> params ;
    private JSONObject jsonObject ;
    private JsonObjectRequest jsonObjectRequest;
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

    public synchronized void requestServer(int method, String url, Map<String, String> params, final Handler handler){
        jsonObject = new JSONObject(params);// 将 Map 转为 JsonObject 的参数

        // 参数：[请求方式][请求链接][请求参数][成功回调][失败回调]
        jsonObjectRequest = new JsonObjectRequest(
                method,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        int code = 0;
                        try {
                            code = response.getInt("status");

                        if (code == UserStateCode.USER_SUCCESS) {
                            Message msg = Message.obtain();
                            msg.what = UserStateCode.USER_SUCCESS;
                            handler.sendMessage(msg);
                            UserState.uId = response.getJSONObject("result").getString("uid");
//                            UserState.referer = response.getString("referer");
                        } else {
                            Message msg = Message.obtain();
                            msg.what = UserStateCode.USER_SIGN_NOMATCH;
                            handler.sendMessage(msg);
                        }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                        msg.what = UserStateCode.REQUEST_ERROR;
                        handler.sendMessage(msg);

                    }
                }){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                Map<String, String> responseHeaders = response.headers;
                UserState.referer = responseHeaders.get("Set-Cookie");
                return super.parseNetworkResponse(response);
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                20 * 1000, 5, 1.0f));
        jsonObjectRequest.setTag("login");// 设置标签
        AppContext.getRequestQueue().add(jsonObjectRequest);// 将请求添加进队列
    }

    public void registerUser(Map<String, String> params, final Handler handler){
        JSONObject jsonObject = new JSONObject(params);// 将 Map 转为 JsonObject 的参数
        String url = Config.HTTPSERVER+"/api/reg";
        // 参数：[请求方式][请求链接][请求参数][成功回调][失败回调]
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        int code = -2;
                        String reponseMsg = null;
                        try {
                            code = response.getInt("status");
                            reponseMsg = response.getString("msg");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(code == 1){
                            Message msg = Message.obtain();
                            msg.what = UserStateCode.USER_SUCCESS;
                            msg.obj = reponseMsg;
                            handler.sendMessage(msg);
                        }else if(code == 0){
                            Message msg = Message.obtain();
                            msg.what = UserStateCode.USER_REGISTER_FAIL;
                            msg.obj = reponseMsg;
                            handler.sendMessage(msg);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Message msg = Message.obtain();
                        msg.what = UserStateCode.USER_LINK_SERVER_FAIL;
                        handler.sendMessage(msg);

                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                20 * 1000, 1, 1.0f));
        jsonObjectRequest.setTag("reg");// 设置标签
        AppContext.getRequestQueue().add(jsonObjectRequest);// 将请求添加进队列
    }

    public void forgetUser(Map<String, String> params, final Handler handler){
        JSONObject jsonObject = new JSONObject(params);// 将 Map 转为 JsonObject 的参数
        String url = Config.HTTPSERVER +"/api/modifyPswd";
        // 参数：[请求方式][请求链接][请求参数][成功回调][失败回调]
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        int code = -2;
                        String reponseMsg = null;
                        try {
                            code = response.getInt("status");
                            reponseMsg = response.getString("msg");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(code == 1){
                            Message msg = Message.obtain();
                            msg.what = UserStateCode.USER_SUCCESS;
                            msg.obj = reponseMsg;
                            handler.sendMessage(msg);
                        }else {
                            Message msg = Message.obtain();
                            msg.what = UserStateCode.USER_FORGET_FAIL;
                            msg.obj = reponseMsg;
                            handler.sendMessage(msg);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Message msg = Message.obtain();
                        msg.what = UserStateCode.USER_LINK_SERVER_FAIL;
                        handler.sendMessage(msg);

                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                20 * 1000, 1, 1.0f));
        jsonObjectRequest.setTag("123456");// 设置标签
        AppContext.getRequestQueue().add(jsonObjectRequest);// 将请求添加进队列
    }


    public void logoutUser(final Handler handler) {
        JSONObject jsonObject = new JSONObject();// 将 Map 转为 JsonObject 的参数
        String url = Config.HTTPSERVER + "/api/logout";
        // 参数：[请求方式][请求链接][请求参数][成功回调][失败回调]
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        int code = -2;
                        String reponseMsg = null;
                        try {
                            code = response.getInt("status");
                            reponseMsg = response.getString("msg");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (code == 1) {
                            Message msg = Message.obtain();
                            msg.what = UserStateCode.USER_SUCCESS;
                            msg.obj = reponseMsg;
                            handler.sendMessage(msg);
                        } else if (code == 0) {
                            Message msg = Message.obtain();
                            msg.what = UserStateCode.USER_LOGOUT_FAIL;
                            msg.obj = reponseMsg;
                            handler.sendMessage(msg);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Message msg = Message.obtain();
                        msg.what = UserStateCode.USER_LINK_SERVER_FAIL;
                        handler.sendMessage(msg);

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Cookie", UserState.referer);
                return params;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                20 * 1000, 1, 1.0f));
        jsonObjectRequest.setTag("logout");// 设置标签
        AppContext.getRequestQueue().add(jsonObjectRequest);// 将请求添加进队列
    }







}

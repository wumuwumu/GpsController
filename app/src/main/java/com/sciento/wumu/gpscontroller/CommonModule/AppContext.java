package com.sciento.wumu.gpscontroller.CommonModule;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by wumu on 17-7-8.
 */

public class AppContext extends Application {

    private static Context instance;
    private static RequestQueue requestQueue;// 全局的请求队列

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = getApplicationContext();
        requestQueue = Volley.newRequestQueue(this);// 实例化请求队列

    }

    public static Context getContext()
    {
        return instance;
    }
    public static RequestQueue getRequestQueue() {  return requestQueue;  }

}
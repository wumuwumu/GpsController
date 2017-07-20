package com.sciento.wumu.gpscontroller.CommonModule;

import android.content.Context;
import android.support.multidex.BuildConfig;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by wumu on 17-7-8.
 */

public class AppContext extends MultiDexApplication {

    private static Context instance;
    private static RequestQueue requestQueue;// 全局的请求队列

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = getApplicationContext();
        requestQueue = Volley.newRequestQueue(this);// 实例化请求队列
        EventBus.builder().throwSubscriberException(BuildConfig.DEBUG).installDefaultEventBus();



    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Context getContext()
    {
        return instance;
    }
    public static RequestQueue getRequestQueue() {  return requestQueue;  }

}
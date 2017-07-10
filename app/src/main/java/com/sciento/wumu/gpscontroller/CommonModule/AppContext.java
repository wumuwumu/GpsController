package com.sciento.wumu.gpscontroller.CommonModule;

import android.app.Application;
import android.content.Context;

/**
 * Created by wumu on 17-7-8.
 */

public class AppContext extends Application {

    private static Context instance;

    @Override
    public void onCreate()
    {
        super.onCreate();
        instance = getApplicationContext();
    }

    public static Context getContext()
    {
        return instance;
    }

}
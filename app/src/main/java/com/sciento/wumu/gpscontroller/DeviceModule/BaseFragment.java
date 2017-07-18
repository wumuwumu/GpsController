package com.sciento.wumu.gpscontroller.DeviceModule;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wumu on 17-7-16.
 */

public class BaseFragment extends Fragment {


    /** 存储器 */
    public SharedPreferences sharedPreferences;

    /** 存储器默认名称 */
    public static final String SPF_Name = "wumu";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        sharedPreferences = getActivity().getSharedPreferences(SPF_Name, Context.MODE_PRIVATE);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}

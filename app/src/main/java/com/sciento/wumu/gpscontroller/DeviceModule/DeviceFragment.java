package com.sciento.wumu.gpscontroller.DeviceModule;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sciento.wumu.gpscontroller.HomeModule.HomeFragment;
import com.sciento.wumu.gpscontroller.R;

/**
 * this is about Device <br>
 * A simple {@link Fragment} subclass.
 * Use the {@link DeviceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeviceFragment extends Fragment {



    public static DeviceFragment newInstance() {
        DeviceFragment fragment = new DeviceFragment();

        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_device, container, false);
    }




}

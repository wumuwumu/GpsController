package com.sciento.wumu.gpscontroller.ControllerModule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sciento.wumu.gpscontroller.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ControllerActivity extends AppCompatActivity {

    @BindView(R.id.lv_controller)
    ListView lvController;


    String deviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        deviceId = bundle.getString("deviceid");
        init();
    }

    private void init() {



        lvController.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Intent switchIntent = new Intent(ControllerActivity.this,SwitchActivity.class);
                        Bundle switchBundle = new Bundle();
                        switchBundle.putString("deviceid",deviceId);
                        switchIntent.putExtras(switchBundle);
                        startActivity(switchIntent);
                        break;
                    case 1:
                        Intent fenceIntent = new Intent(ControllerActivity.this,FenceActivity.class);
                        Bundle fenceBundle = new Bundle();
                        fenceBundle.putString("deviceid",deviceId);
                        fenceIntent.putExtras(fenceBundle);
                        startActivity(fenceIntent);
                        break;
                }
            }
        });

    }
}

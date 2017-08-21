package com.sciento.wumu.gpscontroller.FirstModule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.volley.Request;
import com.sciento.wumu.gpscontroller.CommonModule.MainActivity;
import com.sciento.wumu.gpscontroller.ConfigModule.Config;
import com.sciento.wumu.gpscontroller.ConfigModule.UserState;
import com.sciento.wumu.gpscontroller.ConfigModule.UserStateCode;
import com.sciento.wumu.gpscontroller.MqttModule.LocationToJson;
import com.sciento.wumu.gpscontroller.R;
import com.sciento.wumu.gpscontroller.UserModule.UserLoginActivity;
import com.sciento.wumu.gpscontroller.UserModule.UserNetworkConn;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class FirstActivity extends AppCompatActivity {

    private final int MSG_AUTO_LOGIN = 12;

    Handler firstHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_AUTO_LOGIN:

                    break;

                case UserStateCode.USER_SUCCESS:

                    break;
                case UserStateCode.USER_SIGN_NOMATCH:

                    break;
                case UserStateCode.REQUEST_ERROR:

                    break;

            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        init();
        getHomeActivity();
    }

    private void init() {

    }

    private void getHomeActivity() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("gps", MODE_PRIVATE);

                if (sharedPreferences.getBoolean("remember", false) == true
                        && System.currentTimeMillis() / 1000 - sharedPreferences.getLong("time", 0) < 7 * 24 * 3600) {
                    UserState.username = sharedPreferences.getString("username", "");
                    UserState.referer = sharedPreferences.getString("referer", "");
                    Intent intent = new Intent(FirstActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else {
                    Intent intent = new Intent(FirstActivity.this, UserLoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
                finish();

            }
        };
        timer.schedule(task, 2500);
    }
}

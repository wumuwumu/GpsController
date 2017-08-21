package com.sciento.wumu.gpscontroller.UserModule;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Button;

import com.sciento.wumu.gpscontroller.R;
import com.sciento.wumu.gpscontroller.Utils.ProgressDialogUtils;
import com.sciento.wumu.gpscontroller.Utils.ToastUtils;
import com.sciento.wumu.gpscontroller.Utils.VerifyCodeManager;
import com.sciento.wumu.gpscontroller.View.CleanEditText;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserForgetActivity extends AppCompatActivity {


    @BindView(R.id.tv_forget_mobile)
    CleanEditText tvForgetMobile;
    @BindView(R.id.btn_send_verifi_code)
    Button btnSendVerifiCode;
    @BindView(R.id.tv_forget_identify_code)
    CleanEditText tvForgetIdentifyCode;
    @BindView(R.id.tv_forget_enter_passwd)
    CleanEditText tvForgetEnterPasswd;
    @BindView(R.id.btn_reset_passwd)
    AppCompatButton btnResetPasswd;



    private final int MSG_STATUS = 35;
    Handler registerHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MSG_STATUS:
                    ProgressDialogUtils.getInstance().dismiss();
                    if(((Integer)msg.obj).intValue() ==1){
                        ToastUtils.makeShortText(getString(R.string.str_register_success),UserForgetActivity.this);
                        finish();
                    }else {
                        ToastUtils.makeShortText(getString(R.string.str_register_fail),UserForgetActivity.this);

                    }
                    break;
                case MSG_FAIL:
                    ProgressDialogUtils.getInstance().dismiss();
                    ToastUtils.makeShortText(getString(R.string.str_link_server_fail),UserForgetActivity.this);
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private final int MSG_FAIL = 36;
    private VerifyCodeManager codeManager;
    Handler registerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_STATUS:
                    ProgressDialogUtils.getInstance().dismiss();
                    if (((Integer) msg.obj).intValue() == 1) {
                        ToastUtils.makeShortText(getString(R.string.str_register_success), UserForgetActivity.this);
                        finish();
                    } else {
                        ToastUtils.makeShortText(getString(R.string.str_register_fail), UserForgetActivity.this);

                    }
                    break;
                case MSG_FAIL:
                    ProgressDialogUtils.getInstance().dismiss();
                    ToastUtils.makeShortText(getString(R.string.str_link_server_fail), UserForgetActivity.this);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_forget);
        ButterKnife.bind(this);
         init();
        initEvent();
    }

    private void init() {
        codeManager = new VerifyCodeManager(this, tvForgetMobile, btnSendVerifiCode);
    }

    private void initEvent() {

    }


    @OnClick({
            R.id.btn_reset_passwd,
            R.id.btn_send_verifi_code
    })
    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_reset_passwd:
                ProgressDialogUtils.getInstance().show(UserForgetActivity.this,getString(R.string.str_register_ing));
                Map<String, String> map = new HashMap<>();
                map.put("userName",tvForgetMobile.getText().toString());
                map.put("passwd",tvForgetEnterPasswd.getText().toString());
                UserNetworkConn.getInstance().forgetUser(map,registerHandler);
//                JSONObject jsonObject = new JSONObject(map);// 将 Map 转为 JsonObject 的参数
//                String url = Config.HTTPSERVER +"/api//modifyPswd";
//                // 参数：[请求方式][请求链接][请求参数][成功回调][失败回调]
//                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                        Request.Method.POST,
//                        url,
//                        jsonObject,
//                        new Response.Listener<JSONObject>() {
//
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                int code = 0;
//                                try {
//                                    code = response.getInt("status");
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//                                Message msg = Message.obtain();
//                                msg.what = MSG_STATUS;
//                                msg.obj = code;
//                                registerHandler.sendMessage(msg);
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Message msg = Message.obtain();
//                                msg.what = MSG_FAIL;
//                                registerHandler.sendMessage(msg);
//
//                            }
//                        });
//
//                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
//                        20 * 1000, 1, 1.0f));
//                jsonObjectRequest.setTag("123456");// 设置标签
//                AppContext.getRequestQueue().add(jsonObjectRequest);// 将请求添加进队列
                break;
            case R.id.btn_send_verifi_code:
                codeManager.getVerifyCode(VerifyCodeManager.REGISTER);
                break;

        }
    }
}

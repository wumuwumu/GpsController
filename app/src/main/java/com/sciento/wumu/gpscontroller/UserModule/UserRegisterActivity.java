package com.sciento.wumu.gpscontroller.UserModule;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sciento.wumu.gpscontroller.ConfigModule.UserStateCode;
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

public class UserRegisterActivity extends AppCompatActivity {

    @BindView(R.id.tv_mobile)
    CleanEditText tvMobile;
    @BindView(R.id.btn_send_verifi_code)
    Button btnSendVerifiCode;
    @BindView(R.id.layout_phone)
    LinearLayout layoutPhone;
    @BindView(R.id.tv_identify_code)
    CleanEditText tvIdentifyCode;
    @BindView(R.id.tv_name)
    CleanEditText tvName;
    @BindView(R.id.tv_enter_passwd)
    CleanEditText tvEnterPasswd;
    @BindView(R.id.tv_reEnterPassword)
    CleanEditText tvReEnterPassword;
    @BindView(R.id.btn_signup)
    AppCompatButton btnSignup;
    @BindView(R.id.tv_link_login)
    TextView tvLinkLogin;


    private VerifyCodeManager codeManager;
    private final int MSG_STATUS = 33;
    private final int MSG_FAIL = 34;

    Handler registerHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UserStateCode.USER_SUCCESS:
                    ProgressDialogUtils.getInstance().dismiss();
                        ToastUtils.makeShortText(getString(R.string.str_register_success),UserRegisterActivity.this);
                        finish();
                    break;
                case UserStateCode.USER_REGISTER_FAIL:
                    ProgressDialogUtils.getInstance().dismiss();
                    ToastUtils.makeShortText(msg.obj.toString(),UserRegisterActivity.this);
                case UserStateCode.USER_LINK_SERVER_FAIL:
                    ProgressDialogUtils.getInstance().dismiss();
                    ToastUtils.makeShortText(msg.obj.toString(),UserRegisterActivity.this);
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        init();
        initEvent();
    }

    private void init() {
        codeManager = new VerifyCodeManager(this, tvMobile, btnSendVerifiCode);
    }

    private void initEvent() {
        tvName.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                // 点击虚拟键盘的done
                //if ( actionId == EditorInfo.IME_ACTION_NEXT) {
                    if(tvName.getText().toString().length()<6){
                        tvName.setError(getString(R.string.error_invalid_password));
                    }
               // }
                return false;
            }
        });

        tvReEnterPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                // 点击虚拟键盘的done
                if (actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_GO) {
                    if(tvReEnterPassword.getText().toString()
                            .equals(tvReEnterPassword.getText().toString())){
                        tvReEnterPassword.setError(getString(R.string.error_double_passwd_different));
                    }
                }
                return false;
            }
        });

    }

    @OnClick({
            R.id.btn_send_verifi_code,
            R.id.btn_signup,
            R.id.tv_link_login
    })
    void OnCLick(View view){
        switch (view.getId()){
            case R.id.btn_send_verifi_code:
                codeManager.getVerifyCode(VerifyCodeManager.REGISTER);
                break;
            case R.id.btn_signup:
                if(!tvEnterPasswd.getText().toString().equals(tvReEnterPassword.getText().toString())){
                    ToastUtils.makeShortText(getString(R.string.error_double_passwd_different),UserRegisterActivity.this);
                }
                ProgressDialogUtils.getInstance().show(UserRegisterActivity.this,getString(R.string.str_register_ing));
                Map<String, String> params = new HashMap<>();
                params.put("account", tvMobile.getText().toString());
                params.put("password", tvEnterPasswd.getText().toString());
                UserNetworkConn.getInstance().registerUser(params,registerHandler);
//                JSONObject jsonObject = new JSONObject(params);// 将 Map 转为 JsonObject 的参数
//                String url = Config.HTTPSERVER+"/api/reg";
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
//                jsonObjectRequest.setTag("1234");// 设置标签
//                AppContext.getRequestQueue().add(jsonObjectRequest);// 将请求添加进队列

                break;
            case R.id.tv_link_login:
                Intent linkLoginIntent = new Intent(UserRegisterActivity.this,UserLoginActivity.class);
                startActivity(linkLoginIntent);
                finish();
                break;
        }
    }
}

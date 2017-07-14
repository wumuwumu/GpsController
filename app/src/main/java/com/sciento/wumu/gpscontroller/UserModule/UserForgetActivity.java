package com.sciento.wumu.gpscontroller.UserModule;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.sciento.wumu.gpscontroller.R;
import com.sciento.wumu.gpscontroller.Utils.VerifyCodeManager;
import com.sciento.wumu.gpscontroller.View.CleanEditText;

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


    private VerifyCodeManager codeManager;

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

                break;
            case R.id.btn_send_verifi_code:
                codeManager.getVerifyCode(VerifyCodeManager.REGISTER);
                break;

        }
    }
}

package com.sciento.wumu.gpscontroller.UserModule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sciento.wumu.gpscontroller.R;
import com.sciento.wumu.gpscontroller.Utils.VerifyCodeManager;
import com.sciento.wumu.gpscontroller.View.CleanEditText;

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

                break;
            case R.id.tv_link_login:
                Intent linkLoginIntent = new Intent(UserRegisterActivity.this,UserLoginActivity.class);
                startActivity(linkLoginIntent);
                break;
        }
    }
}

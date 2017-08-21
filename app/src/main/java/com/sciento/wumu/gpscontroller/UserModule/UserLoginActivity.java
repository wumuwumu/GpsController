package com.sciento.wumu.gpscontroller.UserModule;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.sciento.wumu.gpscontroller.CommonModule.MainActivity;
import com.sciento.wumu.gpscontroller.ConfigModule.Config;
import com.sciento.wumu.gpscontroller.ConfigModule.UserState;
import com.sciento.wumu.gpscontroller.ConfigModule.UserStateCode;
import com.sciento.wumu.gpscontroller.R;
import com.sciento.wumu.gpscontroller.Utils.Md5Util;
import com.sciento.wumu.gpscontroller.Utils.ProgressDialogUtils;
import com.sciento.wumu.gpscontroller.Utils.RegexUtils;
import com.sciento.wumu.gpscontroller.Utils.ToastUtils;
import com.sciento.wumu.gpscontroller.View.CleanEditText;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A login screen that offers login via email/password.
 */
public class UserLoginActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SETTING = 12;
    private static final int MSG_AUTOMATIC_LOGIN = 404;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.tv_forget_passwd)
    TextView tvForgetPasswd;
    @BindView(R.id.tv_skip)
    TextView tvSkip;
    @BindView(R.id.cb_remember_passwd)
    CheckBox cbRememberPasswd;
    SharedPreferences sharedPreferences;
    private String phone;
    private String password;
    private boolean remember;
    private UserLoginTask mAuthTask = null;
    // UI references.
    private CleanEditText mPhoneView;
    private CleanEditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    Handler loginHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mAuthTask = null;
            showProgress(false);

            switch (msg.what) {
                case UserStateCode.USER_SUCCESS:
                    UserState.issignin = true;
                    UserState.username = phone;
                    UserState.userpasswd = password;
                    sharedPreferences = getSharedPreferences("gps", MODE_PRIVATE);
                    //得到SharedPreferences.Editor对象，并保存数据到该对象中
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("phone", phone);
                    editor.putString("passwd", password);
                    editor.putBoolean("remember",remember);
                    editor.putString("referer", UserState.referer);
                    editor.putLong("time", System.currentTimeMillis() / 1000);
                    editor.commit();

                    Intent intent = new Intent(UserLoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    ProgressDialogUtils.getInstance().dismiss();
                    finish();
                    break;
                case UserStateCode.USER_SIGN_NOMATCH:
                    ProgressDialogUtils.getInstance().dismiss();
                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                    mPasswordView.requestFocus();
                    break;

                case UserStateCode.REQUEST_ERROR:
                    ProgressDialogUtils.getInstance().dismiss();
                    ToastUtils.makeShortText(getString(R.string.error_login_request), UserLoginActivity.this);
                    break;
                case MSG_AUTOMATIC_LOGIN:
                    sharedPreferences = getSharedPreferences("gps", MODE_PRIVATE);
                    if(sharedPreferences == null){
                        break;
                    }
                    if(sharedPreferences.getBoolean("remember",false) == true){
                        phone = sharedPreferences.getString("phone","15626475082");
                        password = sharedPreferences.getString("passwd","123456");
                        Map<String, String> params = new HashMap<>();
                        params.put("account", phone);
                        params.put("password", password);
                        remember =true;
                        UserNetworkConn.getInstance().requestServer(
                                Request.Method.POST,
                                Config.HTTPSERVER + "/api/login",
                                params,
                                loginHandler
                        );

                    }
                    break;
            }

            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        ButterKnife.bind(this);
        // Set up the login form.
        AndPermission.with(this)
                .requestCode(REQUEST_CODE_SETTING)
                .permission(
                        Manifest.permission.INTERNET
                ).rationale(new RationaleListener() {

            @Override
            public void showRequestPermissionRationale(int arg0, Rationale arg1) {
                AndPermission.rationaleDialog(UserLoginActivity.this, arg1).show();
            }
        }).start();
//        loginHandler.sendEmptyMessage(MSG_AUTOMATIC_LOGIN);
        init();

    }

    private void init() {
        mPhoneView = (CleanEditText) findViewById(R.id.phone);


        mPasswordView = (CleanEditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.phone_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mPhoneView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        phone = mPhoneView.getText().toString();
        password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // 手机验证
        if (TextUtils.isEmpty(phone)) {
            mPhoneView.setError(getString(R.string.error_field_required));
            focusView = mPhoneView;
            cancel = true;
            //Toast.makeText(this, R.string.tip_account_empty, Toast.LENGTH_LONG).show();
        }
        // 账号不匹配手机号格式（11位数字且以1开头）
        else if (!RegexUtils.checkMobile(phone)) {
            mPhoneView.setError(getString(R.string.tip_phone_regex_not_right));
            focusView = mPhoneView;
            cancel = true;
            //Toast.makeText(this, R.string.tip_account_regex_not_right, Toast.LENGTH_LONG).show();
        }

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.tip_password_can_not_be_empty));
            focusView = mPasswordView;
            cancel = true;
            //Toast.makeText(this, R.string.tip_password_can_not_be_empty, Toast.LENGTH_LONG).show();
        } else if (password.length() < 6) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPhoneView;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            ProgressDialogUtils.getInstance().dismiss();
            ProgressDialogUtils.getInstance().show(
                    UserLoginActivity.this, "正在请求...");
            //mAuthTask = new UserLoginTask(phone, password);
            //mAuthTask.execute((Void) null);
            remember = cbRememberPasswd.isChecked();
            Map<String, String> params = new HashMap<>();
            params.put("account", phone);
            params.put("password", password);

            UserNetworkConn.getInstance().requestServer(
                    Request.Method.POST,
                    Config.HTTPSERVER + "/api/login",
                    params,
                    loginHandler
            );


//            JSONObject jsonObject = new JSONObject(params);// 将 Map 转为 JsonObject 的参数
//            // 参数：[请求方式][请求链接][请求参数][成功回调][失败回调]
//            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
//                    Request.Method.POST,
//                    Config.HTTPSERVER+"/api/login",
//                    jsonObject,
//                    new Response.Listener<JSONObject>() {
//
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            int code = 0;
//                            try {
//                                code = response.getInt("status");
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            if (code == UserStateCode.USER_SUCCESS) {
//                                Message msg = Message.obtain();
//                                msg.what = UserStateCode.USER_SUCCESS;
//                                loginHandler.sendMessage(msg);
//                            } else {
//                                Message msg = Message.obtain();
//                                msg.what = UserStateCode.USER_SIGN_NOMATCH;
//                                loginHandler.sendMessage(msg);
//                            }
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//
//                            if (!NetworkUtils.isConnected(UserLoginActivity.this)) {
//                                Toast.makeText(UserLoginActivity.this, R.string.error_network_dis,
//                                        Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(UserLoginActivity.this, R.string.error_link_server,
//                                        Toast.LENGTH_SHORT).show();
//                            }
//
//                            Message msg = Message.obtain();
//                            msg.what = MSG_REQUEST_ERROR;
//                            loginHandler.sendMessage(msg);
//
//                        }
//                    });
//            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
//                    20 * 1000, 1, 1.0f));
//            jsonObjectRequest.setTag("doJsonPost");// 设置标签
//            AppContext.getRequestQueue().add(jsonObjectRequest);// 将请求添加进队列
        }
    }


    @OnClick({R.id.tv_forget_passwd,
            R.id.tv_register,
            R.id.tv_skip})
    void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_forget_passwd:
                Intent foregetIntent = new Intent(UserLoginActivity.this, UserForgetActivity.class);
                startActivity(foregetIntent);
                break;
            case R.id.tv_register:
                Intent registerIntent = new Intent(UserLoginActivity.this, UserRegisterActivity.class);
                startActivity(registerIntent);
                break;
            case R.id.tv_skip:
                Intent skipIntent = new Intent(UserLoginActivity.this, MainActivity.class);
                startActivity(skipIntent);
                finish();
                break;
        }
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(UserLoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        //mPhoneView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }


    /**
     * 异步任务,内部类
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.


            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}


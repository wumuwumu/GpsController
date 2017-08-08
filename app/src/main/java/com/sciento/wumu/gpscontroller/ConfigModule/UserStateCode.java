package com.sciento.wumu.gpscontroller.ConfigModule;

import com.sciento.wumu.gpscontroller.Utils.ProgressDialogUtils;

/**
 * @Author:zpw
 * @Descroption:
 * @Date: create in 22:27 ${DTAE}
 */
public class UserStateCode {

    //user
    public static final int USER_SUCCESS=1;
    public static final int USER_FAIL = 0;

    public static final int USER_NAME_INVALID=205;
    public static final int USER_PSWD_INVALID=201;
    public static final int USER_PSWD_NOMATCH=202;//注册密码不匹配
    public static final int USER_EMAIL_INVALID=203;
    public static final int USER_NAME_EXIST=204;
    public static final int USER_SIGN_NOMATCH=206;//用户名或者密码不匹配
    public static final int REQUEST_ERROR = 207; //请求错误

    //register
    public static final int USER_REGISTER_FAIL = 208;
    public static final int USER_LINK_SERVER_FAIL = 209;

    //forget
    public static final int USER_FORGET_FAIL = 300;
}

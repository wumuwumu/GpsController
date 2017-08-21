package com.sciento.wumu.gpscontroller.Model.Model;

import com.sciento.wumu.gpscontroller.Model.FenceQueryParam;

/**
 * Created by wumu on 17-8-3.
 */

public class FenceInfo {

    private int status;
    private String state;
    private String msg;
    private String referer;
    private FenceQueryParam result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }

    public FenceQueryParam getResult() {
        return result;
    }

    public void setResult(FenceQueryParam result) {
        this.result = result;
    }


}

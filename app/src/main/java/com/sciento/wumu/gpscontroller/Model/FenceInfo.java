package com.sciento.wumu.gpscontroller.Model;

/**
 * Created by wumu on 17-8-3.
 */

public class FenceInfo {

    private int status;
    private String state ;
    private String msg;
    private String referer;
    private Fence result;

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

    public Fence getResult() {
        return result;
    }

    public void setResult(Fence result) {
        this.result = result;
    }




}

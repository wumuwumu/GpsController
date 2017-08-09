package com.sciento.wumu.gpscontroller.Event;

/**
 * Created by wumu on 17-8-7.
 */

public class DeviceConnected {


    String clientid;
    ;
    String ipaddress;
    String username;
    Boolean session;
    int version;
    int connack;
    long ts;


    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Boolean getSession() {
        return session;
    }

    public void setSession(Boolean session) {
        this.session = session;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getConnack() {
        return connack;
    }

    public void setConnack(int connack) {
        this.connack = connack;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }
}

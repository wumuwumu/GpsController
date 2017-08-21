package com.sciento.wumu.gpscontroller.Event;

/**
 * Created by wumu on 17-8-18.
 */

public class NetworkState {
    private boolean state;
    private String networkName;

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }
}

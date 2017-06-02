package com.hb.hbsq.eventbus;

/**
 * Created by wanglin  on 2017/4/5 17:22.
 */

public class EventBean {
    private boolean isFormVIPAd;
    private boolean isOpen;

    public EventBean(boolean isFormVIPAd, boolean isOpen) {
        this.isFormVIPAd = isFormVIPAd;
        this.isOpen = isOpen;
    }

    public boolean isFormVIPAd() {
        return isFormVIPAd;
    }

    public boolean isOpen() {
        return isOpen;
    }
}

package com.hb.hbsq.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by zhangkai on 2017/3/27.
 */

public class NowPayInfo {
    private String partnerid;
    @JSONField(name = "rsmd5")
    private String wx_sign;
    private String starttime;
    private String payway_account_name;
    private String notify_url;


    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getWx_sign() {
        return wx_sign;
    }

    public void setWx_sign(String wx_sign) {
        this.wx_sign = wx_sign;
    }


    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getPayway_account_name() {
        return payway_account_name;
    }

    public void setPayway_account_name(String payway_account_name) {
        this.payway_account_name = payway_account_name;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }


}

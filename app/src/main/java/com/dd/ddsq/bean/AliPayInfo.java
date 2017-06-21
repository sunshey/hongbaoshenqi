package com.dd.ddsq.bean;

/**
 * Created by wanglin  on 2017/3/24 16:07.
 */

public class AliPayInfo {
    private String partnerid;
    private String email;
    private String appid;
    private String privatekey;

    private String payway_name;
    private String notify_url;


    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPrivatekey() {
        return privatekey;
    }

    public void setPrivatekey(String privatekey) {
        this.privatekey = privatekey;
    }

    public String getPayway_name() {
        return payway_name;
    }

    public void setPayway_name(String payway_name) {
        this.payway_name = payway_name;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    @Override
    public String toString() {
        return "AliPayInfo{" +
                "partnerid='" + partnerid + '\'' +
                ", email='" + email + '\'' +
                ", appid='" + appid + '\'' +
                ", privatekey='" + privatekey + '\'' +
                ", payway_name='" + payway_name + '\'' +
                ", notify_url='" + notify_url + '\'' +
                '}';
    }
}

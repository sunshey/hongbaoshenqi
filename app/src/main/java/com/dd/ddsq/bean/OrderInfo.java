package com.dd.ddsq.bean;

import java.io.Serializable;

/**
 * Created by zhangkai on 2017/3/17.
 */

public class OrderInfo implements Serializable {

    private String order_sn;
    private String money;
    private int type;
    private String name;//title
    private String good_id;
    private int good_num;
    private String payway_name;
    private String add_time;
    private String message;
    private String alias;//别名

    private NowPayInfo nowPayInfo;
    private AliPayInfo aliPayInfo;
    private WxPayInfo wxPayInfo;

    public OrderInfo() {
    }

    public OrderInfo(String order_sn, String money, int type, String name, String good_id, int good_num, String payway_name, String add_time, String alias) {
        this.order_sn = order_sn;
        this.money = money;
        this.type = type;
        this.name = name;
        this.good_id = good_id;
        this.good_num = good_num;
        this.payway_name = payway_name;
        this.add_time = add_time;
        this.alias = alias;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGood_id() {
        return good_id;
    }

    public void setGood_id(String good_id) {
        this.good_id = good_id;
    }

    public int getGood_num() {
        return good_num;
    }

    public void setGood_num(int good_num) {
        this.good_num = good_num;
    }

    public String getPayway_name() {
        return payway_name;
    }

    public void setPayway_name(String payway_name) {
        this.payway_name = payway_name;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public NowPayInfo getNowPayInfo() {
        return nowPayInfo;
    }

    public void setNowPayInfo(NowPayInfo nowPayInfo) {
        this.nowPayInfo = nowPayInfo;
    }

    public AliPayInfo getAliPayInfo() {
        return aliPayInfo;
    }

    public void setAliPayInfo(AliPayInfo aliPayInfo) {
        this.aliPayInfo = aliPayInfo;
    }

    public WxPayInfo getWxPayInfo() {
        return wxPayInfo;
    }

    public void setWxPayInfo(WxPayInfo wxPayInfo) {
        this.wxPayInfo = wxPayInfo;
    }

    @Override
    public String toString() {
        return "OrderInfo{" +
                "order_sn='" + order_sn + '\'' +
                ", money='" + money + '\'' +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", good_id='" + good_id + '\'' +
                ", good_num=" + good_num +
                ", payway_name='" + payway_name + '\'' +
                ", add_time='" + add_time + '\'' +
                ", message='" + message + '\'' +
                ", alias='" + alias + '\'' +
                ", nowPayInfo=" + nowPayInfo +
                ", aliPayInfo=" + aliPayInfo +
                '}';
    }
}

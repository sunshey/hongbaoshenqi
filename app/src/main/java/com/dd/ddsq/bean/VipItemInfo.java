package com.dd.ddsq.bean;

import java.io.Serializable;

/**
 * Created by zhangkai on 2017/2/21.
 */

public class VipItemInfo  implements Serializable {


    //app_id": "2",
    /*"id": "9",
    "price": "33.80",
    "real_price": "33.80",
    "title": "抢红包加速"*/
    private String app_id;
    private String id;
    private String price;
    private String real_price;
    private String title;
    private String alias;
    private int pay_way;
    private String payway_name;

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getReal_price() {
        return real_price;
    }

    public void setReal_price(String real_price) {
        this.real_price = real_price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getPay_way() {
        return pay_way;
    }

    public void setPay_way(int pay_way) {
        this.pay_way = pay_way;
    }

    public String getPayway_name() {
        return payway_name;
    }

    public void setPayway_name(String payway_name) {
        this.payway_name = payway_name;
    }

    @Override
    public String toString() {
        return "VipItemInfo{" +
                "app_id='" + app_id + '\'' +
                ", id='" + id + '\'' +
                ", price=" + price +
                ", real_price=" + real_price +
                ", title='" + title + '\'' +
                '}';
    }
}

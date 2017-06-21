package com.dd.ddsq.bean;

/**
 * Created by wanglin  on 2017/3/2 17:53.
 */
public class HongbaoItemInfo {



    private String add_date;
    private String add_time;
    private String from;
    private String id;
    private double money;
    private String payer;

    public String getAdd_date() {
        return add_date;
    }

    public void setAdd_date(String add_date) {
        this.add_date = add_date;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    @Override
    public String toString() {
        return "HongbaoItemInfo{" +
                "add_date='" + add_date + '\'' +
                ", add_time='" + add_time + '\'' +
                ", from='" + from + '\'' +
                ", id='" + id + '\'' +
                ", money=" + money +
                ", payer='" + payer + '\'' +
                '}';
    }
}

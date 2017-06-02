package com.hb.hbsq.bean;

/**
 * Created by wanglin  on 2017/3/2 17:53.
 */
public class HongbaoInfo {
    private String name;
    private String from;
    private double money;
    private String time;
    private String isBest;//是否手气最佳

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIsBest() {
        return isBest;
    }

    public void setIsBest(String isBest) {
        this.isBest = isBest;
    }
}

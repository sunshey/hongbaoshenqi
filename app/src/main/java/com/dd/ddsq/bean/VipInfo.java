package com.dd.ddsq.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Observable;

/**
 * Created by zhangkai on 2017/2/21.
 */

public class VipInfo extends Observable {


    @JSONField(name = "vip_sub_num")
    private int type;

    private int status;
    @JSONField(name = "is_reg")
    private boolean isRegister;

    @JSONField(name = "vip_test_hour")
    private int tryHour;

    @JSONField(name = "user_id")
    private String uid;
    @JSONField(name = "vip_type")
    private int vType;

    private String vip_test_num;


    public String getVip_test_num() {
        return vip_test_num;
    }

    public void setVip_test_num(String vip_test_num) {
        this.vip_test_num = vip_test_num;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUid() {

        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;

    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getvType() {
        return vType;
    }

    public void setvType(int vType) {
        this.vType = vType;
    }

    public int getTryHour() {
        return tryHour;
    }

    public void setTryHour(int tryHour) {
        this.tryHour = tryHour;
    }

    public boolean isRegister() {
        return isRegister;
    }


    public void setRegister(boolean register) {
        isRegister = register;
    }

    public void setDataChanged() {
        setChanged();
    }

}

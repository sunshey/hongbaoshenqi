package com.hb.hbsq.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by wanglin  on 2017/3/3 08:30.
 */
public class PayInfo {
    //    "money": "68.80",
    //    "order_sn": "201703031438403182",
    //    "status": "待支付"

    private String money;
    @JSONField(name = "order_sn")
    private String orderId;
    private String status;

    @JSONField(name = "params")
    private CommonInfo commonInfo;


    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CommonInfo getCommonInfo() {
        return commonInfo;
    }

    public void setCommonInfo(CommonInfo commonInfo) {
        this.commonInfo = commonInfo;
    }


    @Override
    public String toString() {
        return "PayInfo{" +
                "money='" + money + '\'' +
                ", orderId='" + orderId + '\'' +
                ", status='" + status + '\'' +
                ", info=" +
                '}';
    }
}

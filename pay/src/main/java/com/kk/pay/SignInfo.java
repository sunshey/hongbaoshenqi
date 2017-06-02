package com.kk.pay;

/**
 * Created by wanglin  on 2017/6/1 09:58.
 */

public class SignInfo {

    private int resultCode;
    private String sign;
    private String signtype;
    private SignValue info;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSigntype() {
        return signtype;
    }

    public void setSigntype(String signtype) {
        this.signtype = signtype;
    }

    public SignValue getInfo() {
        return info;
    }

    public void setInfo(SignValue info) {
        this.info = info;
    }
}

class SignValue {
    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getSignValue() {
        return signValue;
    }

    public void setSignValue(String signValue) {
        this.signValue = signValue;
    }

    public String getPayurl() {
        return payurl;
    }

    public void setPayurl(String payurl) {
        this.payurl = payurl;
    }

    private String nonceStr;
    private String signValue;
    private String payurl;
}

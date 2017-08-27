package com.kk.pay;

import java.io.Serializable;

/**
 * Created by wanglin  on 2017/6/29 10:14.
 */

public class XjInfo implements Serializable {

    private int err;
    private String code_url;
    private String code_img_url;

    public int getErr() {
        return err;
    }

    public void setErr(int err) {
        this.err = err;
    }

    public String getCode_url() {
        return code_url;
    }

    public void setCode_url(String code_url) {
        this.code_url = code_url;
    }

    public String getCode_img_url() {
        return code_img_url;
    }

    public void setCode_img_url(String code_img_url) {
        this.code_img_url = code_img_url;
    }
}

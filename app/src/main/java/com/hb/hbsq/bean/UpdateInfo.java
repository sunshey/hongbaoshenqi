package com.hb.hbsq.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by wanglin  on 2017/3/8 09:25.
 */

public class UpdateInfo {

    @JSONField(name = "app_url")
    private String url;
    @JSONField(name = "version")
    private String versionID;
    private String desp;//更新说明
    @JSONField(name = "versionCode")
    private int code;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersionID() {
        return versionID;
    }

    public void setVersionID(String versionID) {
        this.versionID = versionID;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

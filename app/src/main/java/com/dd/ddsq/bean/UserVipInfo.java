package com.dd.ddsq.bean;

/**
 * Created by wanglin  on 2017/3/7 16:32.
 * 用户开通的vip项目
 */
public class UserVipInfo {
//    "vip_end_time": "0",
//    "vip_id": "5",
//    "vip_sub_num": "1"
    private String vip_end_time;
    private String vip_id;
    private String vip_sub_num;

    public String getVip_end_time() {
        return vip_end_time;
    }

    public void setVip_end_time(String vip_end_time) {
        this.vip_end_time = vip_end_time;
    }

    public String getVip_sub_num() {
        return vip_sub_num;
    }

    public void setVip_sub_num(String vip_sub_num) {
        this.vip_sub_num = vip_sub_num;
    }

    public String getVip_id() {
        return vip_id;
    }

    public void setVip_id(String vip_id) {
        this.vip_id = vip_id;
    }
}

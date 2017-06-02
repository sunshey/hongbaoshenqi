package com.hb.hbsq.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created by wanglin  on 2017/3/2 17:14.
 */

public class VipItemListInfo {
    @JSONField(name = "vip_list")
    private List<VipItemInfo> vipInfoList;

    public List<VipItemInfo> getVipInfoList() {
        return vipInfoList;
    }

    public void setVipInfoList(List<VipItemInfo> vipInfoList) {
        this.vipInfoList = vipInfoList;
    }
}

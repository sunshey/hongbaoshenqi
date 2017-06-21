package com.dd.ddsq.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created by wanglin  on 2017/3/2 17:52.
 */
public class HongbaoListInfo {
    @JSONField(name = "list")
    private List<HongbaoItemInfo> hongbaoInfoList;

    public List<HongbaoItemInfo> getHongbaoInfoList() {
        return hongbaoInfoList;
    }

    public void setHongbaoInfoList(List<HongbaoItemInfo> hongbaoInfoList) {
        this.hongbaoInfoList = hongbaoInfoList;
    }
}

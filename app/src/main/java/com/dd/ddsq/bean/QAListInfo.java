package com.dd.ddsq.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created by zhangkai on 2017/3/1.
 */

public class QAListInfo {
    public List<QAInfo> getQaInfoList() {
        return qaInfoList;
    }

    public void setQaInfoList(List<QAInfo> qaInfoList) {
        this.qaInfoList = qaInfoList;
    }

    @JSONField(name = "qa_list")
    private List<QAInfo> qaInfoList;

}

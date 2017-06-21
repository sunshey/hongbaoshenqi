package com.dd.ddsq.engine;

import com.dd.ddsq.bean.HongbaoListInfo;
import com.dd.ddsq.config.APPConfig;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin2;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2017/3/2 17:47.
 * 获取红包列表
 */

public class HongbaoListEngin extends BaseEngin2<HongbaoListInfo> {
    @Override
    public String getUrl() {
        return APPConfig.HONGBAO_LIS_URL;
    }


    public Observable<ResultInfo<HongbaoListInfo>> rxGetInfo(String user_id, int page) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("page", page + "");
        return rxpost(HongbaoListInfo.class, params, true, true, true);
    }
}

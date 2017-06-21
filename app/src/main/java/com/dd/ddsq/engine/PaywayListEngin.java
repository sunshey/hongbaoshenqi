package com.dd.ddsq.engine;

import com.dd.ddsq.bean.PaywayListInfo;
import com.dd.ddsq.config.APPConfig;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin2;

import rx.Observable;

/**
 * Created by wanglin  on 2017/3/30 08:38.
 */

public class PaywayListEngin extends BaseEngin2<PaywayListInfo> {
    @Override
    public String getUrl() {
        return APPConfig.PAYWAY_LIST_URL;
    }


    public Observable<ResultInfo<PaywayListInfo>> rxGetInfo(){
        return rxpost(PaywayListInfo.class, null, true, true, true);
    }

}

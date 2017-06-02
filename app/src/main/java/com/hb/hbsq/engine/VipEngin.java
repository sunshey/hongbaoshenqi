package com.hb.hbsq.engine;

import com.hb.hbsq.bean.VipItemListInfo;
import com.hb.hbsq.config.APPConfig;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin2;

import rx.Observable;


/**
 * Created by admin on 2017/3/2.
 */

public class VipEngin extends BaseEngin2<VipItemListInfo> {
    @Override
    public String getUrl() {
        return APPConfig.VIP_LIST_URL;
    }


    public Observable<ResultInfo<VipItemListInfo>> rxGetInfo(){
        return rxpost(VipItemListInfo.class, null, true, true, true);
    }

}

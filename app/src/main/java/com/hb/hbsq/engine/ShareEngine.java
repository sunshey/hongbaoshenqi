package com.hb.hbsq.engine;

import com.hb.hbsq.bean.GoagalInfo;
import com.hb.hbsq.bean.ShareBean;
import com.hb.hbsq.config.APPConfig;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin2;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2017/3/13 19:16.
 */

public class ShareEngine extends BaseEngin2<ShareBean>  {

    @Override
    public String getUrl() {
        return APPConfig.SHARE_URL;
    }

    public Observable<ResultInfo<ShareBean>> rxGetInfo() {
        Map<String, String> params = new HashMap<>();

        params.put("user_id", GoagalInfo.loginDataInfo.getVipInfo().getUid());
        return rxpost(ShareBean.class, params, true, true, true);
    }


}

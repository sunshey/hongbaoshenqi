package com.dd.ddsq.engine;


import com.dd.ddsq.bean.LoginDataInfo;
import com.dd.ddsq.config.APPConfig;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin2;

import rx.Observable;

/**
 * Created by zhangkai on 2017/2/20.
 */

public class LoginEngin extends BaseEngin2<LoginDataInfo> {


    public Observable<ResultInfo<LoginDataInfo>> rxGetInfo() {
        return rxpost(LoginDataInfo.class, null, true, true, true);
    }

    @Override
    public String getUrl() {
        return APPConfig.INIT_URL;
    }


}

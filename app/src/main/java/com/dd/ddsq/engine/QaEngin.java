package com.dd.ddsq.engine;

import com.dd.ddsq.bean.QAListInfo;
import com.dd.ddsq.config.APPConfig;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin2;

import rx.Observable;


/**
 * Created by admin on 2017/3/2.
 */

public class QaEngin extends BaseEngin2<QAListInfo> {
    @Override
    public String getUrl() {
        return APPConfig.QA_LIST_URL;
    }


    public Observable<ResultInfo<QAListInfo>> rxGetInfo(){
        return rxpost(QAListInfo.class, null, true, true, true);
    }


}

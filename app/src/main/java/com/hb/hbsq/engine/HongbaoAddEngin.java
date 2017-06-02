package com.hb.hbsq.engine;

import com.hb.hbsq.bean.HongbaoInfo;
import com.hb.hbsq.config.APPConfig;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin2;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2017/3/2 17:47.
 */

public class HongbaoAddEngin extends BaseEngin2<HongbaoInfo> {
    @Override
    public String getUrl() {
        return APPConfig.HONGBAO_ADD_URL;
    }
//    user_id:用户id
//    payer:发红包的人
//    money：红包金额
//    from：红包来源

    public Observable<ResultInfo<HongbaoInfo>> rxGetInfo(String user_id, String payer, double money, String from) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("payer", payer);
        params.put("money", money + "");
        params.put("from", from);
        return rxpost(HongbaoInfo.class, params, true, true, true);
    }
}

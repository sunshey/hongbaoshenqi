package com.hb.hbsq.engine;

import com.hb.hbsq.bean.PayInfo;
import com.hb.hbsq.config.APPConfig;
import com.kk.securityhttp.domain.ResultInfo;

import com.kk.securityhttp.engin.BaseEngin2;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2017/3/3 08:29.
 */
public class PayEngin extends BaseEngin2<PayInfo> {

    @Override
    public String getUrl() {
        return APPConfig.PAY_URL;
    }


//    user_id:用户id
//    goods_id:商品id(对应vip_list中的id)
//    goods_num:商品数量
//    payway_name:支付方式名称，如iapppay
//    type:0,消费；1：打赏

    public Observable<ResultInfo<PayInfo>> rxGetInfo(String user_id, String goods_id, int goods_num, String payway_name, String md5signstr, int type){
        Map<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("goods_id", goods_id);
        params.put("goods_num", goods_num + "");
        params.put("payway_name", payway_name);
        params.put("type", type + "");
        params.put("is_payway_split", 1 + "");
        params.put("md5signstr", md5signstr + "");
        return rxpost(PayInfo.class, params, true, true, true);
    }
}

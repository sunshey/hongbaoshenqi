package com.kk.pay;


import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin2;


import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2017/3/13 09:13.
 * 验证订单
 */

public class CheckOrderEngin extends BaseEngin2<ResultInfo> {


    @Override
    public String getUrl() {
        return "http://u.wk990.com/api/index/orders_check?app_id=2";
    }

    public Observable<ResultInfo<ResultInfo>> rxGetInfo(OrderInfo orderInfo) {
        Map<String, String> params = new HashMap<>();

        params.put("order_sn", orderInfo.getOrder_sn());
        params.put("good_id", orderInfo.getGood_id() + "");
        params.put("money", orderInfo.getMoney() + "");
        params.put("title", orderInfo.getName());
        params.put("goods_num", orderInfo.getGood_num() + "");
        params.put("payway_name", orderInfo.getPayway());
        params.put("type", orderInfo.getType() + "");
        params.put("add_time", orderInfo.getAddtime());
        params.put("is_payway_split", 1 + "");


        return rxpost(ResultInfo.class, params, true, true, true);
    }


}

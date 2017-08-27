package com.kk.pay;

import android.app.Activity;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kk.pay.other.LoadingDialog;
import com.kk.pay.other.LogUtil;
import com.kk.pay.other.PayCodeFragment;
import com.kk.pay.other.PayCodeFragmentNew;
import com.kk.pay.other.ToastUtil;
import com.switfpass.pay.thread.NetHelper;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wanglin  on 2017/6/29 08:49.
 */

public class IXJPayImpl extends IPayImpl {
    private String url = "http://121.196.213.236/xjpay";

    public IXJPayImpl(Activity context) {
        super(context);
        loadingDialog =new LoadingDialog(context);
        isGen = true;
    }

    @Override
    public void pay(final OrderInfo orderInfo, final IPayCallback iPayCallback) {
        if (orderInfo == null || iPayCallback == null) {
            ToastUtil.toast2(mContext, "支付失败");
            return;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("cid", 1087);
        BigDecimal price = new BigDecimal(orderInfo.getMoney() * 100 + "");
        params.put("total_fee", price.intValue() + "");
        params.put("title", orderInfo.getName());
        params.put("attach", orderInfo.getName());
        params.put("platform", "CR");
        params.put("orderno", orderInfo.getOrder_sn());
        final String result = JSON.toJSONString(params);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String s = NetHelper.getInstance().HttpPost(url, result);
                LogUtil.msg("result  " + s);
                final XjInfo xjInfo = JSONObject.parseObject(s, XjInfo.class);
                if (xjInfo != null && xjInfo.getErr() == 0) {
                    mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            PayCodeFragmentNew payCodeFragment = new PayCodeFragmentNew();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("xjInfo", xjInfo);
                            payCodeFragment.setArguments(bundle);
                            payCodeFragment.setOderInfo(orderInfo);
                            payCodeFragment.setIpaycallBack(iPayCallback);
                            payCodeFragment.show(mContext.getFragmentManager(), null);
                        }
                    });

                } else {
                    mContext.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            ToastUtil.toast2(mContext, "支付失败");
                        }
                    });
                }

            }
        }).start();

    }


}

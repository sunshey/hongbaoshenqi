package com.kk.pay;

import android.app.Activity;

import com.kk.pay.other.ToastUtil;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.switfpass.pay.thread.Executable;
import com.switfpass.pay.thread.NetHelper;
import com.switfpass.pay.thread.NotifyListener;
import com.switfpass.pay.thread.ThreadHelper;
import com.switfpass.pay.utils.MD5;
import com.switfpass.pay.utils.SignUtils;
import com.switfpass.pay.utils.XmlUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by zhangkai on 2017/6/26.
 */

public class IShiftPassPayImpl extends IPayImpl {

    public static String url = "http://pay.shiftpass.cn/gateway/";

    public IShiftPassPayImpl(Activity context) {
        super(context);
    }


    @Override
    public void pay(OrderInfo orderInfo, IPayCallback iPayCallback) {
        if (orderInfo == null || orderInfo.getPayInfo() == null) {
            ToastUtil.toast2(mContext, "支付失败");
            return;
        }

        PayInfo payInfo = orderInfo.getPayInfo();
        final Map params = new HashMap();
        params.put("service", "pay.weixin.wappay");
        params.put("sign_type", "MD5");
        params.put("mch_id", "904170629809");
        params.put("out_trade_no", orderInfo.getOrder_sn());
        params.put("body", orderInfo.getName());

        BigDecimal price = new BigDecimal(orderInfo.getMoney()*100 + "");
        params.put("total_fee", price);

        params.put("notify_url", payInfo.getPayurl());
        params.put("mch_app_name", "扬扬助手");
        params.put("mch_app_id", "com.kk.pay");
        params.put("nonce_str", randNonce());


        StringBuilder buf = new StringBuilder((params.size() +1) * 10);
        SignUtils.buildPayParams(buf,params,false);
        String sign = MD5.md5s(buf.toString() + "&key=017e28785e1f4b3896e7e4c3fc78babc").toUpperCase();
        params.put("sign", sign);
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = NetHelper.getInstance().HttpPost(url, XmlUtils.toXml(params));
                HashMap mapResult = XmlUtils.parse(result);
                if(mapResult != null){

                }
            }
        }).start();


    }

    private String randNonce(){
        return new Random().nextInt(1000000000)+"";
    }


}

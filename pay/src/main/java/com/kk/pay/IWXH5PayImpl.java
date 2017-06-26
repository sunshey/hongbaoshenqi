package com.kk.pay;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.KeyEvent;

import com.kk.pay.other.LoadingDialog;
import com.kk.pay.other.LogUtil;
import com.kk.pay.other.ToastUtil;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.kk.securityhttp.engin.HttpCoreEngin2;
import com.kk.securityhttp.net.contains.HttpConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.ipaynow.plugin.conf.PluginConfig.context;


/**
 * Created by zhangkai on 2017/4/26.
 */

public class IWXH5PayImpl extends IPayImpl {


    public IWXH5PayImpl(Activity context) {
        super(context);
        loadingDialog = new LoadingDialog(context);
        isGen = true;
    }


    @Override
    public void pay(final OrderInfo orderInfo, final IPayCallback iPayCallback) {
        if (orderInfo == null || orderInfo.getPayInfo() == null) {
            ToastUtil.toast2(mContext, "支付失败");
            return;
        }
        PayInfo payInfo = orderInfo.getPayInfo();
        Map<String, String> params = new HashMap<>();
        params.put("partnerid", payInfo.getPartnerid());
        params.put("order_sn", orderInfo.getOrder_sn());
        params.put("goods_title", orderInfo.getName());
        params.put("money", orderInfo.getMoney() + "");
        params.put("front_notify_url", payInfo.getFrontnotifyurl());
        params.put("notify_url", payInfo.getNotify_url());
        params.put("starttime", payInfo.getStarttime());

        HttpCoreEngin2.get().rxpost(payInfo.getPayurl() + appidStr, String.class, params, true, true, true).observeOn
                (AndroidSchedulers.mainThread()).subscribe(new Action1<ResultInfo<String>>() {
            @Override
            public void call(ResultInfo<String> resultInfo) {

                if (resultInfo.code == HttpConfig.STATUS_OK) {
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(resultInfo.data));
                        mContext.startActivity(intent);
                        IPayImpl.uiPayCallback = iPayCallback;
                        IPayImpl.uOrderInfo = orderInfo;
                        isGen = true;
                    } catch (Exception e) {
                        ToastUtil.toast2(mContext, "支付错误");
                        LogUtil.msg("Exception  " + e.getMessage());
                    }
                }
            }
        });
    }


}

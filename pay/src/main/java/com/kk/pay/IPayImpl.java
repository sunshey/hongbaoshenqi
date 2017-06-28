package com.kk.pay;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Handler;
import android.view.KeyEvent;

import com.kk.pay.other.LoadingDialog;
import com.kk.pay.other.LogUtil;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin2;
import com.kk.securityhttp.net.contains.HttpConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/3/17.
 */

public abstract class IPayImpl {

    protected static final String appidStr = "?app_id=2";

    public static boolean isGen() {
        return isGen;
    }

    public static void setIsGen(boolean isGen) {
        IPayImpl.isGen = isGen;
    }

    protected static boolean isGen = false;

    public static OrderInfo uOrderInfo;
    public static IPayCallback uiPayCallback;
    public static String appid;
    protected Activity mContext;

    public Handler mHandler = new Handler();

    public IPayImpl(Activity context) {
        this.mContext = context;
    }

    public abstract void pay(OrderInfo orderInfo, IPayCallback iPayCallback);

    public Object befor(Object... obj) {
        return null;
    }

    public Object after(Object... obj) {
        return null;
    }

    protected String get(String cStr, String dStr) {
        return cStr == null || cStr.isEmpty() ? dStr : cStr;
    }


    protected static LoadingDialog loadingDialog;
    private static int n = 0;
    private static int times = 5;

    public static void checkOrder(final OrderInfo orderInfo, final IPayCallback iPayCallback) {

        if (!isGen) return;
        if (loadingDialog == null) return;
        if (n == 0) {
            loadingDialog.setCancelable(false);
            loadingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                    }
                    return false;
                }
            });

            loadingDialog.show("正在查询...");
        }
        String url = "http://u.wk990.com/api/index/orders_query" + appidStr;
        Map<String, String> params = new HashMap<>();
        params.put("order_sn", orderInfo.getOrder_sn());
        HttpCoreEngin2.get().rxpost(url, String.class, params, true, true, true).delay(3, TimeUnit.SECONDS).observeOn
                (AndroidSchedulers.mainThread()).subscribe(new Action1<ResultInfo<String>>() {
            @Override
            public void call(ResultInfo<String> resultInfo) {

                if (resultInfo != null && resultInfo.code == HttpConfig.STATUS_OK) {
                    if (loadingDialog.isShowing()) loadingDialog.dismiss();

                    orderInfo.setMessage("支付成功");

                    iPayCallback.onSuccess(orderInfo);
                    IPayImpl.uiPayCallback = null;
                    IPayImpl.uOrderInfo = null;
                    isGen = false;
                    return;
                }
                n++;
                if (n < times) {
                    checkOrder(orderInfo, iPayCallback);
                } else {
                    if (loadingDialog.isShowing()) loadingDialog.dismiss();
                    orderInfo.setMessage("支付失败");
                    iPayCallback.onFailure(orderInfo);
                    IPayImpl.uiPayCallback = null;
                    IPayImpl.uOrderInfo = null;
                    isGen = false;
                    n = 0;
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        });
    }
}

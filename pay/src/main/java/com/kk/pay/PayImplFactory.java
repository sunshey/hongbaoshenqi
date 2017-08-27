package com.kk.pay;

import android.app.Activity;
import android.text.style.TtsSpan;

/**
 * Created by zhangkai on 2017/4/17.
 */

public class PayImplFactory {
    public static IPayImpl createPayImpl(Activity context, String name) {

        if (context == null || name == null || name.isEmpty()) {
            return null;
        }

        IPayImpl iPayImpl = null;

        if (name.equals("xjhy_wxpay")) {
        } else if (name.equals("alipay")) {
            iPayImpl = new IAliPay1Impl(context);
        } else if (name.equals("ipaynow_wxpay")) {
            iPayImpl = new INowPayImpl(context, "13");
        } else if (name.equals("ipaynow_alipay")) {
            iPayImpl = new INowPayImpl(context, "12");
        } else if (name.equals("wxpay")) {
            iPayImpl = new IWXPay1Impl(context);
        } else if ("ipaynowh5".equals(name)) {
            iPayImpl = new IWXH5PayImpl(context);
        } else if ("xxpay".equals(name)) {
            iPayImpl = new IXxPay2Impl(context);
        } else if ("spwxpay".equals(name)) {
            iPayImpl = new IShiftPassPayImpl(context);
        } else if ("xjhy".equals(name)) {
            iPayImpl = new IXJPayImpl(context);
        } else if ("spalipay".equals(name)) {
            iPayImpl = new IShiftPassAliPayImpl(context);
        } else if ("dunxingyun_alipay".equals(name)) {
            iPayImpl = new IDunXingPayImpl(context, "1");
        } else if ("dunxingyun_wxpay".equals(name)) {
            iPayImpl = new IDunXingPayImpl(context, "0");
        }

        return iPayImpl;
    }
}

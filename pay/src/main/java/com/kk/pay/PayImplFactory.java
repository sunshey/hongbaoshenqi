package com.kk.pay;

import android.app.Activity;

/**
 * Created by zhangkai on 2017/4/17.
 */

public class PayImplFactory {
    public static IPayImpl createPayImpl(Activity context, String name) {

        if (name == null || name.isEmpty()) {
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
        }

        return iPayImpl;
    }
}

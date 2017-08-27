package com.kk.pay;

import android.app.Activity;

import com.kk.pay.other.ToastUtil;

import dxtx.dj.pay.api.PayuPlugin;
import dxtx.dj.pay.pay_util.enums.PayType;
import dxtx.dj.pay.pay_util.ui.OrderBean;
import dxtx.dj.pay.pay_util.ui.PayBack;

/**
 * Created by zhangkai on 2017/8/24.
 */

public class IDunXingPayImpl extends IPayImpl {

    private PayType mType;
    public static final String appKey = "D14BFF78F653BF5C524C432EB1CC044F80096127911C0575";
    public static final String appId = "81";

    public IDunXingPayImpl(Activity context, String type) {
        super(context);
        if (type.equals("0")) {
            mType = PayType.PAY_WX;
        } else {
            mType = PayType.PAY_ZFB;
        }
        PayuPlugin.getPayPlugin().init(context, appKey, appId, null);
    }

    @Override
    public void pay(final OrderInfo orderInfo, final IPayCallback iPayCallback) {
        if (orderInfo == null || orderInfo.getPayInfo() == null) {
            ToastUtil.toast2(mContext, "支付失败");
            return;
        }

        final PayInfo payInfo = orderInfo.getPayInfo();

        OrderBean orderBean = new OrderBean(orderInfo.getOrder_sn(), payInfo.getNotify_url(), mType, orderInfo.getName(),
                Double.parseDouble(orderInfo.getMoney()+""),
                "");
        PayuPlugin.getPayPlugin().pay(mContext, appKey, appId, orderBean, new PayBack() {
            @Override
            public void success() {
                orderInfo.setMessage("支付成功");
                iPayCallback.onSuccess(orderInfo);
            }

            @Override
            public void failure(int i, String s) {
                orderInfo.setMessage("支付失败" + i + s);
                iPayCallback.onFailure(orderInfo);
            }
        });
    }

}

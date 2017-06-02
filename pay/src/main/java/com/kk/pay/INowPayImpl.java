package com.kk.pay;

import android.app.Activity;

import com.ipaynow.plugin.api.IpaynowPlugin;
import com.ipaynow.plugin.manager.route.dto.ResponseParams;
import com.ipaynow.plugin.manager.route.impl.ReceivePayResult;
import com.ipaynow.plugin.utils.PreSignMessageUtil;
import com.kk.pay.other.LogUtil;
import com.kk.pay.other.ToastUtil;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.securityhttp.security.Md5;

import java.math.BigDecimal;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/3/27.
 */

public class INowPayImpl extends IPayImpl {
    private IpaynowPlugin mIpaynowplugin;
    public static String WX_APPID = "wx1e439a1d3f45f343";

    private static final String mAppID = "149000702874718";//1490861997750118 自己的
    private static final String mKey = "jBoiF6eUthOt2Xq9XB9G9iDw50Dg77Yh";

    //微信支付
    private PreSignMessageUtil preSign = new PreSignMessageUtil();
    private String type;

    public INowPayImpl(Activity context, String type) {
        super(context);
        this.type = type;
        if (mIpaynowplugin == null) {
            mIpaynowplugin = IpaynowPlugin.getInstance().init(context);// 1.插件初始化
            mIpaynowplugin.unCkeckEnvironment();
        }
    }


    @Override
    public void pay(OrderInfo orderInfo, IPayCallback iPayCallback) {
        if (orderInfo.getPayInfo() == null) {
            return;
        }
        uOrderInfo = orderInfo;
        uiPayCallback = iPayCallback;

        PayInfo payInfo = orderInfo.getPayInfo();
        preSign.appId = get(payInfo.getAppid(), payInfo.getPartnerid());
//        preSign.appId = mAppID;
        preSign.mhtOrderNo = orderInfo.getOrder_sn();
        preSign.notifyUrl = get(payInfo.getNotify_url() + "", "");
        preSign.mhtOrderStartTime = payInfo.getStarttime();
        String wx_sign = payInfo.getWx_sign();
//        String wx_sign = mKey;

        String preSignStr = preSign.generatePreSignMessage();
        final String needcheckmsg = preSignStr + "&" + "mhtSignature="
                + Md5.md5(preSignStr + "&" + Md5.md5(wx_sign)) + "&mhtSignType=MD5";// 0nqIDgkOnNBD6qoqO5U68RO1fNqiaisg

        String mhtSignature = preSignStr + "&mhtSignature=" + wx_sign
                + "&mhtSignType=MD5";
        mIpaynowplugin.setCallResultReceiver(new ReceivePayResult() {
            @Override
            public void onIpaynowTransResult(ResponseParams responseParams) {
                onPayResult(uOrderInfo, uiPayCallback, responseParams);
            }
        }).pay(mhtSignature);
    }

    private void onPayResult(OrderInfo orderInfo, IPayCallback iPayCallback, ResponseParams arg0) {
        String respCode = arg0.respCode;
        String errorCode = arg0.errorCode;
        String errorMsg = arg0.respMsg;
        StringBuilder temp = new StringBuilder();
        if (respCode.equals("00")) {
            temp.append("交易状态:成功");
            iPayCallback.onSuccess(orderInfo);
            orderInfo.setMessage("支付成功");
            checkOrder(orderInfo);
        } else if (respCode.equals("02")) {
            temp.append("交易状态:取消");
            iPayCallback.onFailure(orderInfo);
        } else if (respCode.equals("01")) {
            iPayCallback.onFailure(orderInfo);
            temp.append("交易状态:失败").append("\n").append("错误码:").append(errorCode).append("原因:" + errorMsg);
        } else if (respCode.equals("03")) {
            iPayCallback.onFailure(orderInfo);
            temp.append("交易状态:未知").append("\n").append("原因:" + errorMsg);
        } else {
            iPayCallback.onFailure(orderInfo);
            temp.append("respCode=").append(respCode).append("\n").append("respMsg=").append(errorMsg);
        }
        LogUtil.msg("temp:  " + temp.toString() + "   errorMsg  " + errorMsg);
        ToastUtil.toast(mContext, temp.toString());

    }

    private void prePayMessage(float money, String name) {
        preSign.appId = "{appid}";

        preSign.mhtCharset = "UTF-8";
        preSign.mhtCurrencyType = "156";
        // 支付金额
        BigDecimal bd = new BigDecimal(money + "0");
        preSign.mhtOrderAmt = Integer.toString(bd.multiply(new BigDecimal(100)).intValue());
        preSign.mhtOrderDetail = name;
        preSign.mhtOrderName = name;
        preSign.mhtOrderNo = "{orderid}";
        preSign.payChannelType = type;

        preSign.mhtOrderStartTime = "{starttime}";
        preSign.mhtSubAppId = WX_APPID;
        preSign.mhtOrderTimeOut = "10000";
        preSign.mhtOrderType = "01";
        preSign.notifyUrl = "{notify_url}";
        preSign.mhtReserved = "nowpay";
    }

    @Override
    public Object befor(Object... obj) {
        prePayMessage(Float.parseFloat(obj[0] + ""), obj[1] + "");
        return preSign.generatePreSignMessage();
    }

    private void checkOrder(final OrderInfo orderInfo) {
        CheckOrderEngin checkOrderEngin = new CheckOrderEngin();
        checkOrderEngin.rxGetInfo(orderInfo).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ResultInfo<ResultInfo>>() {
            @Override
            public void call(ResultInfo<ResultInfo> resultInfo) {
                if (resultInfo != null && resultInfo.code == HttpConfig.STATUS_OK) {
                    return;
                }
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkOrder(orderInfo);
                    }
                }, 5000);
            }
        });

    }
}

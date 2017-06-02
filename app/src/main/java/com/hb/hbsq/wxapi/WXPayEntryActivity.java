package com.hb.hbsq.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.hb.hbsq.engine.CheckOrderEngin;
import com.hb.hbsq.util.ToastUtil;
import com.kk.pay.IPayImpl;
import com.kk.pay.OrderInfo;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;
    private Handler mHandler = new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, IPayImpl.appid); //appid需换成商户自己开放平台appid
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX && IPayImpl.uOrderInfo != null && IPayImpl.uiPayCallback != null) {
            // resp.errCode == -1 原因：支付错误,可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等
            // resp.errCode == -2 原因 用户取消,无需处理。发生场景：用户不支付了，点击取消，返回APP
            if (resp.errCode == 0) //支付成功
            {
                IPayImpl.uOrderInfo.setMessage("支付成功");
                IPayImpl.uiPayCallback.onSuccess(IPayImpl.uOrderInfo);
                ToastUtil.showToast(this, "支付成功", Toast.LENGTH_SHORT);
                checkOrder(IPayImpl.uOrderInfo);
            } else if (resp.errCode == -1) // 支付错误
            {
                IPayImpl.uOrderInfo.setMessage("支付错误");
                IPayImpl.uiPayCallback.onFailure(IPayImpl.uOrderInfo);
                ToastUtil.showToast(this, "支付错误", Toast.LENGTH_SHORT);

            } else if (resp.errCode == -2) // 支付取消
            {
                IPayImpl.uOrderInfo.setMessage("支付取消");
                IPayImpl.uiPayCallback.onFailure(IPayImpl.uOrderInfo);
                ToastUtil.showToast(this, "支付取消", Toast.LENGTH_SHORT);

            } else {
                IPayImpl.uOrderInfo.setMessage("支付失败");
                IPayImpl.uiPayCallback.onFailure(IPayImpl.uOrderInfo);
                ToastUtil.showToast(this, "支付失败", Toast.LENGTH_SHORT);
            }
            IPayImpl.uOrderInfo = null;
            IPayImpl.uiPayCallback = null;
            finish();
        }
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